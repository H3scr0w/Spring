# Notifier Stress Test

The Notifier stress test Hazelcast cluster and Websockets communications.
This app uses Javascript with nodejs modules such as Jquery, SocketJS and StompJS

## Get started

### Installation

Use package-lock.json in order to install all required modules with following command:

`npm install`

### Configuration

Open a shell command and disable TLS to avoid certificate issue:

`export NODE_TLS_REJECT_UNAUTHORIZED=0`

Open stress_test.js file and update starter vars such as `jwt` with a valid one

You will find also vars such as urls like following example which call sandbox apis:

`var maxConnexions = 3000;`

`var url = "https://notifier.atlas-sbx1.int.sgdbf.saint-gobain.net:20003/notifier/subscribe" + '?Authorization=${jwt}';`

`var destination = "/topic/mep-notification";`

`var publisherApi = "http://ingress.ibm.ppr.docker4sg.saint-gobain.net:20004/send";`

`var notifierApi = "https://notifier.atlas-sbx1.int.sgdbf.saint-gobain.net:20003/send";`


### Run

Run stress test script with the following command:

`node stress_test.js`

You can run multiple script with `run-multi-subscribe.bat` file in order to avoid websockets limitation from nodejs server

`./run-multi-subscribe.bat`

By default, it will open 3 shell windows and each will run stress_test.js script.

`SET /A "count = 3"`




 




