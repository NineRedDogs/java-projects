#!/bin/bash
#
# This script does the following:-
#
# when it detects a new nst-mrb.log.1 file it renames it to cp-nst-mrb-<time and date>.log (eg cp-nst-mrb-125903-09022014.log)
# this is done so we don't have the same log entries in more than one file
# Optionally it then moves this file to a directory - just in case the product is installed in a small partition (if a directory is given with the -v option)
#
# it keeps only up to 50 of these renamed files (deleting old ones once there are more than 50)
# hence the max size of these rolled over logs will be 50 * 10M = 500M
#
# This should allow your copying of whole directories to do what we want - as this script will ensure the logs won't fill
# the partition and will ensure the log entries will not be duplicated in log files (yes we will have duplicate files - but these are easily filtered).

no_rollover_files_to_keep=1000
move_to_dir=""


ADAPTOR_DIR="/opt/ms-adaptor/"
ADAPTOR_BASE_LOG_FILE_NAME="nst-ms-adaptor"

BOOTSTRAP_DIR="/opt/nst-loadbalancer/"
BOOTSTRAP_BASE_LOG_FILE_NAME="nst-bootstrap"

MRB_DIR="/opt/mrb/"
MRB_BASE_LOG_FILE_NAME="nst-mrb"

LB_BASE_LOG_FILE_NAME="nst-lb"

while getopts "?habmk:v:l:" options
	do
	case $options in
		a)
			DIR="$ADAPTOR_DIR"
			BASE_LOG_FILE_NAME="$ADAPTOR_BASE_LOG_FILE_NAME"
			;;
		b)
			DIR="$BOOTSTRAP_DIR/"
			BASE_LOG_FILE_NAME="$BOOTSTRAP_BASE_LOG_FILE_NAME"
			;;
		l)
			DIR=$OPTARG
			BASE_LOG_FILE_NAME="$LB_BASE_LOG_FILE_NAME"
			;;
		m)
			DIR="$MRB_DIR"
			BASE_LOG_FILE_NAME="$MRB_BASE_LOG_FILE_NAME"
			;;
		k)
			no_rollover_files_to_keep=$OPTARG
			;;
		v)
			move_to_dir="$OPTARG/"
			;;
		p)
			pretend=1
			;;
		h|?|*)
			usage
			;;
		esac
	done

usage()
{
echo "Usage:-"
echo "   -a             copy logs for the ms-adaptor ($ADAPTOR_BASE_LOG_FILE_NAME.log files from directory $ADAPTOR_DIR)"
echo "   -b             copy logs for the lb bootstrap ($BOOTSTRAP_BASE_LOG_FILE_NAME.log files from directory $BOOTSTRAP_DIR)"
echo "   -k <no files>  keep the number of rollover files (defaults to $no_rollover_files_to_keep)"
echo "   -l <directory> copy logs for a LB process ($LB_BASE_LOG_FILE_NAME.log files from the given directory)"
echo "   -m             copy logs for the mrb ($BASE_LOG_FILE_NAME.log files from directory $DIR)"
echo "   -v <directory> move rollover files to the given directory (if not given logs are kept in the directory they were created in)"
echo
exit 1
}

if [[ -z $DIR || -z $BASE_LOG_FILE_NAME ]]
	then
	usage
	fi


cd "$DIR"

LOG_FILE_DOT_1="$BASE_LOG_FILE_NAME.log.1"
rollover_logs_prefix="cp-$BASE_LOG_FILE_NAME-"

i=10

echo "Checking for existing rollover log files"
while (( i > 1 ))
	do
	log_file="$BASE_LOG_FILE_NAME.log.$i"
	echo "checking $log_file"
	if [[ -f $log_file ]]
		then
		now=$(date +"%H%M%S-%d%m%Y")
		new_log_file_name="$move_to_dir$rollover_logs_prefix$now.log"
		echo mv $log_file "$new_log_file_name"
		mv $log_file "$new_log_file_name"
		bzip2 "$new_log_file_name"
		fi
	(( i-- ))
	done

echo
echo "Checking for new rollover log files"
while (( 1 ))
	do
	if [[ -f $LOG_FILE_DOT_1 ]]
		then
		now=$(date +"%H%M%S-%d%m%Y")
		new_log_file_name="$move_to_dir$rollover_logs_prefix$now.log"
		echo mv $LOG_FILE_DOT_1 "$new_log_file_name"
		mv $LOG_FILE_DOT_1 "$new_log_file_name"
		bzip2 "$new_log_file_name"
		fi
	ls -t1 $move_to_dir$rollover_logs_prefix* 2>/dev/null | tail -n +$no_rollover_files_to_keep | xargs rm 2>/dev/null
	sleep 10
	done

