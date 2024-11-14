const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/quizies'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/question', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
    stompClient.subscribe('/topic/gameevent', (gameEventMessage) => {
        //gameEvent(JSON.parse(gameEventMessage.body).content);
        gameEvent(JSON.parse(gameEventMessage.body));
        //gameEvent(gameEventMessage);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

/*
function sendName() {
    stompClient.publish({
        destination: "/app/question",
        //body: JSON.stringify({'name': $("#name").val()})
        body: JSON.stringify({'name': 'Diego'})
    });
}
 */

function startGameEvent() {
    stompClient.publish({
        destination: "/app/gameevent",
        body: JSON.stringify({'event': 'StartGame'})
    });
}

function requestQuestionEvent() {
    questions_categories = categories; // update this to remove done categories
    stompClient.publish({
        destination: "/app/gameevent",
        body: JSON.stringify(
            {'event': 'Question',
            'arguments': categories}
        )
    })
}


function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function gameEvent(gameEvent) {
    eventType = gameEvent.type;
    switch (eventType) {
        case "StartGame":
            //gameEvent.categories;
            categories = gameEvent.categories;
            updateCategoryNames(categories);
            nextQuestionServer();
            break;
        case "Question":
            question = gameEvent.question
            loadQuestion(question);
            break;
        default:
            console.log("GameEvent -> EventType -> " + eventType + " -> Default case");
    }
}

function nextQuestionServer() {
    //currentQuestionIndex++;

    if ((currentCategory in score) && (score[currentCategory] > 3)) {
        alert("Categoría completada");
        currentCategory = (currentCategory + 1) % 6;
        //loadQuestion();
        requestQuestionEvent();
    } else {
        //loadQuestion();
        requestQuestionEvent();
    }
/*
    if (currentCategory in score) {
        // categories are loaded
        if (score[currentCategory] < 3) {
            //loadQuestion();
            requestQuestionEvent();
        } else {
            alert("Categoría completada");
            currentCategory = (currentCategory + 1) % 6;
            //loadQuestion();
            requestQuestionEvent();
        }
    }
 */
}

$(function () {
    /*
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
    */
    connect();
    setTimeout(startGameEvent, 1000);
});
