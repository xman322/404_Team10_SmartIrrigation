#include "WiFi.h"
#include <HTTPClient.h>
#include "TimeLib.h"
#include <Arduino_JSON.h>
#include <string.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <stdio.h>
#include <stdlib.h>

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);

int LED_BUILTIN = 23; // io number, NOT pin number
int MODE_PIN = 27; 
int INCREMENT_PIN = 14;
int SELECT_PIN = 13;
int DECREMENT_PIN = 12;
int STOP_PIN = 32;  //STOP PIN
int SENSOR_ITERATOR; // = ??; need a pin lol

float tempTimeMicro; //timing
float tempTimeSec;
float tempTimeMin;
String SystemTime;
float internalTimer;

const char* ssid = "Crossing Place_slow"; //wifi
const char* password = "onmyhonor";
String WiFiIP;

String city = "Bryan"; //weather and time
String country = "US";
String API_Key = "7f283ceb40d49c17fe527532b9fd5e11";
String WeatherURL = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "," + country + "&APPID=" + API_Key;
int TimeInMinutes;
String TimeURL;

bool Day1Rain;
bool Day2Rain;
bool Day3Rain;
bool Day4Rain;
bool Day5Rain;

int TIME = 0;
int MANUAL_HEAD= 1;
//int CYCLES_WEEK = 1; not being used
float INCHES_WEEK = 1;
int ACTIVATE_TIME = 0; //add a default activation time, 5:00 am?
uint8_t MODE = 0; // mode iterator, 0 = smart, 1 = simple, 2 = manual
float SENSOR_INCHES = 0;
int timeSinceLastRain = 0;


void setup() {
  Serial.begin(9600);
  Serial.println(WeatherURL);

  WiFi.mode(WIFI_STA);
  int n = WiFi.scanNetworks();
  Serial.println(n);
  for (int i =0; i< n; i++) {
    Serial.println(WiFi.SSID(i));
  }
  WiFi.begin(ssid, password);
  Serial.print("connecting to wifi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(750);
  }
  

  Serial.println(WiFi.localIP());
  WiFiIP = WiFi.localIP().toString().c_str();
  

  delay(500);
  pinMode (MODE, INPUT);
  pinMode (LED_BUILTIN, OUTPUT);
  pinMode (INCREMENT_PIN, INPUT);
  pinMode (SELECT_PIN, INPUT);
  pinMode (DECREMENT_PIN, INPUT);
  pinMode (SENSOR_ITERATOR, INPUT);
  pinMode (STOP_PIN, INPUT);
  pinMode (OUTPUT_PIN, OUTPUT)

  //MODE0Setup(); //Run start mode start sequence on start?
}
//CONSIDER REMOVING OVERFLOW/UNDERFLOW ***********

static void MODE0_1Setup() { //smart mode start sequence
  while (digitalRead(SELECT_PIN) != HIGH) {
      if (digitalRead(MODE_PIN) == HIGH) {
        delay(750);
        MODE++;
        return;
      }
      if (digitalRead(INCREMENT_PIN) == HIGH) {
        delay(750);
        ACTIVATE_TIME += 30; //not in minutes, will need to be adjusted accordingly. Possibly in ms? ********
      }
      if (digitalRead(DECREMENT_PIN) == HIGH) {
        delay(750);
        ACTIVATE_TIME -= 30; //also not in minutes, will need to be adjusted. ********
      } 
      if (ACTIVATE_TIME > 1440) { //not in minutes, adjust accordingly ******
        ACTIVATE_TIME = 0; //accomidating overflow
        Serial.println("OVER");
      }
      if (ACTIVATE_TIME < 0) { 
        ACTIVATE_TIME = 1440; //accomidating underflow. not in minutes, adjust accordingly ********
        Serial.println("UNDER");
      }
    Serial.println(ACTIVATE_TIME);
    Serial.println("ACTIVATE TIME"); 
    //delay(400);
  }
  delay(1000);
  while (digitalRead(SELECT_PIN) != HIGH) {
    if (digitalRead(INCREMENT_PIN) == HIGH) {
        delay(750);
        INCHES_WEEK = INCHES_WEEK + .1;
      }
      if (digitalRead(DECREMENT_PIN) == HIGH) {
        delay(750);
        INCHES_WEEK = INCHES_WEEK - .1; 
      } 
      if (INCHES_WEEK > 5) { //adding a maximum of 5 inches per week?
        INCHES_WEEK = 0; //accomidating overflow
      }
      if (INCHES_WEEK < 0) { 
        INCHES_WEEK = 5; //accomidating underflow. not in minutes, adjust accordingly ********
      }
    Serial.println(INCHES_WEEK);
    Serial.println("INCHES WEEK");
  }
  delay(1000);
}

static void MODE2Setup() {
  while (digitalRead(SELECT_PIN) != HIGH) {
    if (digitalRead(INCREMENT_PIN) == HIGH) {
        delay(400);
        MANUAL_HEAD += 1;
      }
      if (digitalRead(DECREMENT_PIN) == HIGH) {
        delay(400);
        MANUAL_HEAD -= 1; 
      } 
      if (MANUAL_HEAD > 10) { //adding a maximum of 5 inches per week?
        MANUAL_HEAD = 1; //accomidating overflow
      }
      if (MANUAL_HEAD < 0) { 
        MANUAL_HEAD = 10; //accomidating underflow. not in minutes, adjust accordingly ********
      }
  }
} //manual mode start sequence

String httpGETRequest(String webName) { //FINISHED
  WiFiClient client;
  HTTPClient http;
    
  // Your Domain name with URL path or IP address with path
  http.begin(client, webName);
  int httpResponseCode = http.GET();
  
  String payload = "{}"; 
  
  if (httpResponseCode>0) {
    //Serial.print("HTTP Response code: ");
    //Serial.println(httpResponseCode);
    payload = http.getString();
  }
  http.end();

  //Serial.println(payload);
  return payload;
  
}

static int getTime() { //FINISHED
  String TimeURL = "https://timeapi.io/api/Time/current/zone?timeZone=Europe/Amsterdam";
  //Serial.println(TimeURL);
  //char TimeData[] = httpGETRequest(TimeURL);
  //Serial.println(TimeData);
  //deserializeJson(doc, TimeData);
  
  
  timeClient.update();
  //Serial.println(timeClient.getFormattedTime());
  
  char hours1 = timeClient.getFormattedTime()[0];  
  char hours2 = timeClient.getFormattedTime()[1];
  char minutes1 = timeClient.getFormattedTime()[3];
  char minutes2 = timeClient.getFormattedTime()[4];
  if (hours2 >= 53) {
    hours2 = hours2 -5;
  }
  else {
    hours1 = hours1 -1;
    hours2 = hours2 + 5;
  }
  //Serial.print("HOURS1: "); Serial.println(hours1);
  //Serial.print("Hours2: "); Serial.println(hours2);
  TimeInMinutes = ((hours1-48) * 600) + ((hours2-48) * 60) + ((minutes1-48) * 10) + (minutes2-48);
  Serial.print("TIME IN MINUTES: "); Serial.println(TimeInMinutes);
  return TimeInMinutes;
  //String temp = hours1 + hours2;
  //Serial.print(temp);
  //int hours = temp;
  
  //for (int i = 0; i < 70; i++) {
    //if (TimeData[i] == 84) {
      //char Hours1 = TimeData[i+1]; //Serial.println(Hours1);// + TimeData[i+2];
      //char Hours2 = TimeData[i+2]; //Serial.println(Hours2);
      //char Minutes1 = TimeData[i+4]; //Serial.println(Minutes1);// + TimeData[i+5];
      //char Minutes2 = TimeData[i+5]; //Serial.println(Minutes2);
      //int tempArray[4] = {Hours1, Hours2, Minutes1, Minutes2};
      //TimeInMinutes = ((Hours1-48) * 600) + ((Hours2-48) * 60) + ((Minutes1-48) * 10) + (Minutes2-48);
      //Serial.println(TimeInMinutes);
    //}
  //}
}

void getWeather() { //FINISHED
  String WeatherData = httpGETRequest(WeatherURL);
  //Serial.println(WeatherData);
  int MainIterator = 0;  
  String tempHolder;
  for (int i = 0; i < 50000; i ++) {
    if (WeatherData[i] == 'm') {
      if (WeatherData[i+1] == 'a') {
        if (WeatherData[i+2] == 'i') {
          //Serial.println("MAIN FOUND");
          MainIterator = MainIterator +1;
        }
        if (MainIterator % 2 == 0) {
          //Serial.println(WeatherData[i+7]);
          //Serial.println("<- we want R");    
          if (MainIterator >= 2 && MainIterator <16 ) {
            if (WeatherData[i+7] == 'R') {
              Day1Rain = true;
            }
            //Serial.print("day 1->");
            //Serial.println(Day1Rain);
          }
          if (MainIterator >= 18 && MainIterator <32 ) {
            if (WeatherData[i+7] == 'R') {
              Day2Rain = true;
            }
            //Serial.print("day 2->");
            //Serial.println(Day2Rain);
          }
          if (MainIterator >= 34 && MainIterator <48 ) {
            if (WeatherData[i+7] == 'R') {
              Day3Rain = true;
            }
            //Serial.print("day 3->");
            //Serial.println(Day3Rain);
            
          }
          if (MainIterator >= 50 && MainIterator <64 ) {
            if (WeatherData[i+7] == 'R') {
              Day4Rain = true;
            }
            //Serial.print("day 4->");
            //Serial.println(Day4Rain);
          }
          if (MainIterator >= 68 && MainIterator <82 ) {
            if (WeatherData[i+7] == 'R') {
              Day5Rain = true;
            }
            //Serial.print("day 5->");
            //Serial.println(Day5Rain);
          }
        }
      }
    }
  }
}

static bool shouldItRain(){  
  //timeSinceLastRain variable.
  int temp = millis();
  timeSinceLastRain = (temp /60000) - internalTimer; // -> in minutes - (minutes value @ last rain) = time in minutes since last rain
  float daysSinceLastRain = timeSinceLastRain/1440;
  float inchesNeeded = INCHES_WEEK - SENSOR_INCHES;
  float WeightedForecast = ((Day1Rain * 4) + (Day2Rain * 3) + (Day3Rain * 2) + (Day4Rain * .75) + (Day5Rain * .25))/10;
  float FinalValue = ((daysSinceLastRain * 5) - (WeightedForecast * 7) + (inchesNeeded * 2)) / 2.2;
  if (WeightedForecast >= .7) {
    FinalValue = 0;
  }
  if (inchesNeeded <= 0) {
    FinalValue = 0;
  }
  if (FinalValue >= 1) {
    return true;
  } else {
    return false;
  }
}

void loop() {
  Serial.println(MODE);
  if (digitalRead(MODE_PIN) == HIGH) {
    MODE++;
    delay(1000);
    if (MODE == 3) {
      MODE = 0;
    }
    if (MODE == 0) {
      MODE0_1Setup();
    } if (MODE ==1) {
      MODE0_1Setup();
    } else {
      MODE2Setup();
    }
  }
  /*Serial.println("activate time:");
  Serial.println(ACTIVATE_TIME);
  Serial.println("Inches week:");
  Serial.println(INCHES_WEEK);
  Serial.println("Cycles week:");
  Serial.println(CYCLES_WEEK);*/
  delay(500);
  getWeather();

  Serial.print("Day 1 Rain?: "); Serial.println(Day1Rain);
  Serial.print("Day 2 Rain?: "); Serial.println(Day2Rain);
  Serial.print("Day 3 Rain?: "); Serial.println(Day3Rain);
  Serial.print("Day 4 Rain?: "); Serial.println(Day4Rain);
  Serial.print("Day 5 Rain?: "); Serial.println(Day5Rain);
  getTime();
  Serial.print("Time in minutes at location: "); Serial.println(TimeInMinutes);
  if (MODE == 0) {
    //control sprinklers all complex-like. idle loop for when mode = 0.
    delay(1800); //ADD 2 more 0s for 3 minute delay
    getTime();
    if (SENSOR_ITERATOR == HIGH) {
      SENSOR_INCHES += .01;
      internalTimer = millis() / 60000;
    }
    if (timeSinceLastRain < getTime()) {
      
    }
    
  } //end of mode 0
  if (MODE == 1) { ///WHEN SPRINKLER RUNS, ADD AMNT OF WATER TO SENSOR MEASUREMENT
    getTime();
    if (((ACTIVATE_TIME-5) < TimeInMinutes) && ((ACTIVATE_TIME+5) > TimeInMinutes)) {

    }
  }
  if (MODE == 2) {
    while (STOP_PIN == LOW) {
      OUTPUT_PIN == HIGH;
    }
    MODE = 0;
  }
}

