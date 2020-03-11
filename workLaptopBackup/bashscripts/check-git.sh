#!/bin/bash

# script to build from scratch the full nginx/auth0 PoC

# constants
PARENT_DIR=$(pwd)

# define individual project areas
SCA_AUTH_DIR=$PARENT_DIR/sca-auth
INSURANCE_APP_DIR=$PARENT_DIR/sca-auth-mock-insurance-api
LANDING_PAGE_DIR=$PARENT_DIR/sca-auth-mock-landing-page
LOANS_APP_DIR=$PARENT_DIR/sca-auth-mock-loans-api
INSURANCE_PORTAL_APP_DIR=$PARENT_DIR/sca-auth-mock-insurance-portal
LOANS_PORTAL_APP_DIR=$PARENT_DIR/sca-auth-mock-loans-portal


echo " ##########################################################"; 
echo "  (1) checking insurance portal  ..."; echo;
cd $INSURANCE_PORTAL_APP_DIR
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (2) checking loans portal  ..."; echo;
cd $LOANS_PORTAL_APP_DIR
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (3) checking insurance api  ..."; echo;
cd $INSURANCE_APP_DIR
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (4) checking loans api  ..."; echo;
cd $LOANS_APP_DIR
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (5) checking landing page  ..."; echo;
cd $LANDING_PAGE_DIR
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (6) checking sac auth proxy ..."; echo;
cd $SCA_AUTH_DIR
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo; echo; echo;
