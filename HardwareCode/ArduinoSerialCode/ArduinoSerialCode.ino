int incomingByte = 0;   // for incoming serial data
void setup() {
        Serial.begin(115200);     // opens serial port, sets data rate to 115200 bps
        Serial3.begin(115200);
}

void loop() {
        // send data only when you receive data:
        if (Serial3.available() > 0) {
                // read the incoming byte:
                incomingByte = Serial3.read();
                //Serial.println(incomingByte);
        }
       
        //use this code if the readings are off:
        //Serial.print("a1b2c3d4;1:");
        //Serial.print(150 - (incomingByte - 133) * 2 );
        //Serial.println(";");
        
        Serial.print("a1b2c3d4;1:");
        Serial.print(150 - (incomingByte - 97)*8 );
        Serial.println(";");
        
}
