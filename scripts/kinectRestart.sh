echo "Reiniciando java clients"
for i in $(ps axu |grep -i java|grep -v grep|grep -v eclipse|awk '{print $2}'); do  kill -9 $i; done

buses=$(lsusb |grep -i Microsoft|cut -d' ' -f2|sort -u) #es el mismo para los tres sub-devices.
devices=$(lsusb |grep -i Microsoft|cut -d' ' -f4|sed 's/://g') #tiene 3 devices

echo "Reiniciando Kinect Devices"
for bus in $(echo $buses)
do
  for device in $(echo $devices)
  do
    echo "Reseteando $bus/$device"
    ./usbReset /dev/bus/usb/$bus/$device
  done
done
echo "Done"
