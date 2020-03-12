#!/bin/bash
echo "Extracting install files"
sudo sh -c "cat /tmp/transfer.tar.bz2.* | tar -Pxvjf -"
echo "Install VNFM Service Additions..."
# Copy all .service files under the VNFM directory to the /etc/systemd/system directory
sudo find /opt/dialogic/vnfm -name "*.service" -execdir sh -c 'bn=$(basename $0); cp $0 /etc/systemd/system; chmod 655 /etc/systemd/system/$bn' {} \;
# Enable the vnfm-helper service to try to detect VNFM environment at boot time
sudo sh -c '. /opt/dialogic/common_functions.sh; enable_service vnfm-helper'


