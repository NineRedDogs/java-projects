#!/bin/bash

function cont {
    echo " "
    echo $1
    select yn in "Yes" "No"; do
        case $yn in
            Yes ) echo "continuing ..."; echo; break;;
            No ) echo "quitting ..."; echo; exit;;
        esac
    done
}


##
# set up PRODUCT
###
if [ "$1" == 'mrb' ]; then
    PRODUCT="mrb"
elif [ "$1" == 'lb' ]; then
    PRODUCT="nst-loadbalancer"
elif [ "$1" == 'adaptor' ]; then
    PRODUCT="adaptor"
else
    echo " "; echo "No PRODUCT arg provided (i.e. mrb/lb/adaptor) ... so defaulting to:  mrb"; echo " "
    PRODUCT="mrb"
fi

##
# set up installation directory
###
INSTALL_DIR="/opt/$PRODUCT"
#INSTALL_DIR="/opt/$PRODUCT-slave"

#continue ?
cont "Happy that $PRODUCT is installed to $INSTALL_DIR ? "


##
# stop services
###
service $PRODUCT stop 
service jetty stop
service nst-vip-manager stop

##
# show service status
###
service $PRODUCT status
service jetty status
service nst-vip-manager status

#continue ?
cont "Happy with the state of the services ?"


ps -ef | grep java

# continue ?
cont "Happy with the java processes still running ?"

##
# remove product files ....
###
rm -rfv $INSTALL_DIR
rm -fv /etc/init.d/$PRODUCT
rm -fv /etc/sysconfig/$PRODUCT.properties 

rm -fv /etc/init.d/nst-vip-manager 
rm -fv /etc/init.d/jetty 
rm -fv /etc/sysconfig/nst-vip-manager.properties 

# possibly there's some TLS temp dirs to remove ...
rm -rfv /tmp/certs
rm -rfv /tmp/keys


echo " "
echo " "
echo "All Done!!!"
echo " "



