#include "mbed.h"
#include "EthernetInterface.h"
#include "C12832.h"

#include <string>
// Network interface
EthernetInterface net;
EthernetInterface net2;

static C12832 lcd(D11, D13, D12, D7, D10);
static PwmOut spkr(D6);
InterruptIn up(A2);
InterruptIn down(A3);
InterruptIn left(A4);
InterruptIn right(A5);
TCPSocket socket;
TCPSocket socket2;
int station_id = 1;
int history = 1;
bool flag = false;
void sound() {
    while (1) {
        for (float i=2000.0; i<10000.0; i+=100) {
            spkr.period(1.0/i);
            spkr=0.5;
            wait(0.02);
        }
        spkr=0.0;
    }
    }

void levels() {
  //char *url = "GET /rob/mbed_floodwarning?read_sensor=%d&timestamp=%d";
    char dest[56];
//strcpy( dest, url );
sprintf(dest, "GET /rob/mbed_floodwarning?read_sensor=%d&timestamp=%d\r\n", station_id,history);  
//strcpy( dest, itoa(count));
    int scount = socket.send(dest, sizeof dest);
    char rbuffer[64];
    int rcount = socket.recv(rbuffer, sizeof rbuffer);
    lcd.printf("%.*s\n", strstr(rbuffer, "\r\n")-rbuffer, rbuffer);
    
    
    }
    
    void flooded() {
  char *url = "GET /rob/mbed_floodwarning?mac=";
    char dest[52];
   const char *mac_address =  net.get_mac_address();
strcpy( dest, url );
strcat( dest, mac_address);
strcat( dest, "\r\n");    
    int scount = socket.send(dest, sizeof dest);

    char rbuffer[64];
    int rcount = socket.recv(rbuffer, sizeof rbuffer);
    lcd.printf("%.*s\n", strstr(rbuffer, "\r\n")-rbuffer, rbuffer);
 
    if(strstr(rbuffer,"flooded") != NULL) {
        sound();
        }
        }
  
void fup() {
   station_id++;
   flag = true;

   

   }
   
   
void fdown() {
   if (station_id <= 1) {
       lcd.printf("no more stations");
       } else {
   station_id--;
   flag = true;


   }
   }

  
int main() {
    // Bring up the ethernet interface
    net.connect();
    socket.open(&net);
   
    
    socket.connect("129.12.44.32", 80);
    
    
    lcd.printf("connected\n");

    up.fall(fup);
    down.fall(fdown);
    levels();


    // Show the network address
    
    // Open a socket on the network interface, and create a TCP connection to mbed.org
  
    int sleep_time = 0;
    while (true) {

    // Send a simple http request
       sleep();
        if (flag) {
        levels();
        flag = false;
        
         }
         wait(5);
         sleep_time++;
         
         if ( sleep_time >= 12) {
             flooded();
             }
             
        
        lcd.cls();}
    
    // Close the socket to return its memory and bring down the network interface
    
    }