#include <deprecated.h>
#include <MFRC522.h>
#include <MFRC522Extended.h>
#include <require_cpp11.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SPI.h>
#include <time.h>

// Set these to run example.
#define SS_PIN 2  //D2
#define RST_PIN 0 //D1
#define FIREBASE_HOST "iotproject2-4855b.firebaseio.com"
#define FIREBASE_AUTH "stEt54dIIREk9MDqq1v25HxfA36pbGYgOBJ723jG"
#define WIFI_SSID "TTNET_ZyXEL_4W7X"
#define WIFI_PASSWORD "a7dCf9c63F752"
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.
int statuss = 0;
int out = 0;
int kartdurum=0;
int timezone = 3;
int dst = 0;
time_t baslangic;
time_t bitis;
String Kart;
uint8_t control = 0x00;
void setup() {
  SPI.begin();      // Initiate  SPI bus
  mfrc522.PCD_Init();   // Initiate MFRC522
  Serial.begin(9600);
  

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  configTime(3 * 3600, 0, "pool.ntp.org", "time.nist.gov");
  Serial.println("\nWaiting for time");
  while (!time(nullptr)) {
    Serial.print(".");
    delay(1000);
  }
  Serial.println("");
}

int n = 0;

void loop() {
    // Look for new cards
  if ( !mfrc522.PICC_IsNewCardPresent()) {
    return;
  }
  
  if ( !mfrc522.PICC_ReadCardSerial()) {
    return;
  }
  time_t baslangic = time(nullptr);
  Serial.print(" UID tag :");
  String content= "";
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++) 
  {
     Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
     Serial.print(mfrc522.uid.uidByte[i], HEX);
     content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
     content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }
  content.toUpperCase();
  Kart=String (content.substring(1));
  Serial.println(Kart);
 while(true){
    control=0;
    for(int i=0; i<3; i++){
      if(!mfrc522.PICC_IsNewCardPresent()){
        if(mfrc522.PICC_ReadCardSerial()){
          //Serial.print('a');
          control |= 0x16;
        }
        if(mfrc522.PICC_ReadCardSerial()){
          //Serial.print('b');
          control |= 0x16;
        }
        //Serial.print('c');
          control += 0x1;
      }
      //Serial.print('d');
      control += 0x4;
    }
    
    //Serial.println(control);
    if(control == 13 || control == 14){
      Serial.println("Burada");
      delay(500);
    } else {
      break; 
    }
 }
 delay(500); //change value if you want to read cards faster  
 time_t bitis = time(nullptr);
 String istasyon="A5";
 String sondurum=istasyon+"-"+ctime(&baslangic)+"*"+ctime(&bitis);

 Serial.println(sondurum);
 String Girdi ="logs/"+Kart;
 String name=Firebase.pushString(Girdi," ");
 delay(1000);
 String Girdi1=Girdi+"/"+name;
 String Girdiic1=Girdi1+"/giriscikis";
 String Girdiic2=Girdi1+"/Durum";
 Firebase.remove(Girdi1);
 Firebase.setBool(Girdiic2,true);
 Firebase.setString(Girdi1+"/giris",ctime(&baslangic));
 Firebase.setString(Girdi1+"/cikis",ctime(&bitis));
  // handle error
  if (Firebase.failed()) {
      Serial.print("log atma basarisiz:");
      Serial.println(Firebase.error());  
      return;
  }
  delay(1000);
  mfrc522.PICC_HaltA();
  mfrc522.PCD_StopCrypto1(); 
}


