
#include <ESP8266WiFi.h>

//how many clients should be able to telnet to this ESP8266
#define MAX_SRV_CLIENTS 5
const char* ssid = "UHWireless";
const char* password = "";
#define ledPin 16
#define fumeSensor 5
#define buzzer 4
#define analogInput 0

WiFiServer server(23);
WiFiClient serverClients[MAX_SRV_CLIENTS];

void setup() {
  //----setup pins
  pinMode(ledPin, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(fumeSensor, INPUT);
  pinMode(analogInput, INPUT);
  //----end 
  WiFi.begin(ssid, password);
  //serial for the arduino
  Serial.begin(115200);
  Serial.print("\nConnecting to "); Serial.println(ssid);
  uint8_t i = 0;
  while (WiFi.status() != WL_CONNECTED && i++ < 20) delay(500);
  if(i == 21){
    Serial.print("Could not connect to"); Serial.println(ssid);
    while(1) delay(500);
  }
  //start UART and the server
  server.begin();
  server.setNoDelay(true);
  
  Serial.print("Ready! Use 'telnet ");
  Serial.print(WiFi.localIP());
  Serial.println(" 23' to connect");
  //attachInterrupt(digitalPinToInterrupt(fumeSensor), FSensor, RISING);
}
void FSensor(){
  for(int i = 0; i < MAX_SRV_CLIENTS; i++){
    if (serverClients[i] && serverClients[i].connected()){
      serverClients[i].write("FIRE......!\n");
    }
  }
    for(int i = 0; i < 500; i++){
      digitalWrite(buzzer,HIGH);
      delay(1);
      digitalWrite(buzzer,LOW);
      delay(1);
    }
}

void loop() {
  uint8_t i;
  //check if there are any new clients
  if (server.hasClient()){
    for(i = 0; i < MAX_SRV_CLIENTS; i++){
      //find free/disconnected spot
      if (!serverClients[i] || !serverClients[i].connected()){
        if(serverClients[i]) serverClients[i].stop();
        WiFiClient temp = server.available();
        if(temp){
          serverClients[i] = temp;
          Serial.print("New client: "); Serial.println(i);
        }
      }
    }
    //no free/disconnected spot so reject
    WiFiClient serverClient = server.available();
    serverClient.stop();
  }
  //check clients for data
  for(i = 0; i < MAX_SRV_CLIENTS; i++){
    if (serverClients[i] && serverClients[i].connected()){
      if(serverClients[i].available()){
        //get data from the telnet client and push it to the UART
        if(serverClients[i].available()) ledData(serverClients[i].readString(),i);
      }
    }
  }
  if(digitalRead(fumeSensor) == HIGH){
    FSensor();
  }
}
void ledData(String input,int clientNum){
  //Serial.write(input);
  if(input.startsWith("LED")){
    if(input.charAt(3) == '1'){
      digitalWrite(ledPin,HIGH);
      serverClients[clientNum].write("LED1\n");
    }else{
      digitalWrite(ledPin,LOW);
      serverClients[clientNum].write("LED0 \n");
    }
  }
}
