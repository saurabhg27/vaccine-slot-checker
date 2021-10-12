java -jar target/vac-chk-0.0.1-SNAPSHOT.jar TWILIO_KEY TWILIO_API_ID 10 18 > log.log 2>&1 &
echo $! > pid
cat pid
