#!/bin/bash

# script to revert any changes made to k8s/deployment.yml files by the refresh scripts

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
echo "  (1) reverting k8s/deployment.yaml file in insurance portal  ..."; echo;
cd $INSURANCE_PORTAL_APP_DIR
git checkout -- k8s/deployment.yaml
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (2) reverting k8s/deployment.yaml file in loans portal  ..."; echo;
cd $LOANS_PORTAL_APP_DIR
git checkout -- k8s/deployment.yaml
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (3) reverting k8s/deployment.yaml file in insurance api  ..."; echo;
cd $INSURANCE_APP_DIR
git checkout -- k8s/deployment.yaml
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (4) reverting k8s/deployment.yaml file in loans api  ..."; echo;
cd $LOANS_APP_DIR
git checkout -- k8s/deployment.yaml
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (5) reverting k8s/deployment.yaml file in landing page  ..."; echo;
cd $LANDING_PAGE_DIR
git checkout -- k8s/deployment.yaml
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo " ##########################################################"; 
echo "  (6) reverting k8s/deployment.yaml file in SCA auth proxy ..."; echo;
cd $SCA_AUTH_DIR
git checkout -- k8s/deployment.yaml
git status .
echo " - - - - - - - -  - - - - - - - -  - - - - - - - -  - - - - - - - - " 

echo; echo; echo;
