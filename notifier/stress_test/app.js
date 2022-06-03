var stompClient = null;
var connexions = 0;
var subscriptions = 0;
var publishMessages = 0;
var receiveMessages = 0;
var httpResponses = 0;


function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
	$("#destination").prop("disabled", true);
  }
  else {
    $("#conversation").hide();
	$("#destination").prop("disabled", false);
  }
  $("#greetings").html("");
}

function connect() {

    const jwt = $('#jwt').val();
    const url = $('#url').val() + `?Authorization=${jwt}`;	
	const destination = $("#destination").val();
	
    var socket = new SockJS(url);
    var stompClient = Stomp.over(socket);
      stompClient.connect({Authorization: `sgdbf ${jwt}`}, function () {
		  
	  	  connexions++;
		  subscriptions++;
          setConnected(true);
		  
          stompClient.subscribe(destination, function (greeting) {
             showGreeting(greeting.body);
            }, {Authorization: `sgdbf ${jwt}`}
		  );
		  
		  sendName();
    });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
}

function sendName() {
  const jwt = $('#jwt').val();
  const message = $('#message').val();
  const name = $("#channel").val();
  const destination = $("#destination").val();
  const api = $('#api').val();
  var data = JSON.stringify({"type":"mep-notification","recipients":[],"body":"stress test sbx1 notifier"});
  
 //stompClient.send(destination, {}, message); 
 
  var xhttp = new XMLHttpRequest();
  xhttp.withCredentials = true;
  
  xhttp.addEventListener("readystatechange", function() {
    if(this.readyState === 4 && [200,201,202,203,204].includes(this.status)) {
  	  httpResponses++;      
    }
  });
  xhttp.open("POST", api);
  xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

  xhttp.setRequestHeader("Authorization", `sgdbf ${jwt}`); 
  xhttp.send(data); 
  
  publishMessages++;
}

function showGreeting(message) {
	receiveMessages++;
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function log() {
	console.log("SUBSCRIBER => " + 
				"Connexions: "+connexions+", "+
				"Subscriptions: "+subscriptions+", "+
				"PublishMessages: "+publishMessages+", "+
				"ReceiveMessages: "+receiveMessages+", "+
				"HttpResponses: "+httpResponses
				);
	$("#connexions").html("Connexions: "+connexions);
	$("#subscriptions").html("Subscriptions: "+subscriptions);
	$("#publishMessages").html("PublishMessages: "+publishMessages);
	$("#receiveMessages").html("ReceiveMessages: "+receiveMessages);
	$("#httpResponses").html("HttpResponses: "+httpResponses);
}

function sleep(milliseconds) {
  const date = Date.now();
  let currentDate = null;
  do {
    currentDate = Date.now();
  } while (currentDate - date < milliseconds);
}

function run() {
	var arr = [...Array(1).keys()];
	for (cnb in arr) {
		sleep(50);
		connect();
	}
}

$(function() {
  $("form").on('submit', function(e) {
    e.preventDefault();
  });
  $("#connect").click(function() {
    connect();
  });
  $("#disconnect").click(function() {
    disconnect();
  });
  $("#send").click(function() {
    sendName();
  });
  $("#run").click(function() {
	setInterval(log, 1000);
    run();
  });
});

