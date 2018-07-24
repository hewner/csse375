# Example docker container that allows sends via gmail
#
# Designed using instructions found here:
# https://rtcamp.com/tutorials/linux/ubuntu-postfix-gmail-smtp/
#
# Build me like this (when in this local directory):
# sudo docker build -t nameForContainer . 
FROM ubuntu

RUN apt-get update
RUN apt-get install -y postfix mailutils libsasl2-2 ca-certificates libsasl2-modules
ADD main.cf /etc/postfix/
ADD sasl_passwd /etc/postfix/
ADD mailname /etc/

RUN chmod 400 /etc/postfix/sasl_passwd
RUN postmap /etc/postfix/sasl_passwd

RUN cat /etc/ssl/certs/Thawte_Premium_Server_CA.pem | sudo tee -a /etc/postfix/cacert.pem

# note this installs what's needed, but you must actually start the
# postfix daemon before you can actually send mail
#
# To start the daemon:
#
# service postfix start
#
# To send a message through gmail
#
# echo "Message body" | mail -s "Message Subject" receiver@wherever.com