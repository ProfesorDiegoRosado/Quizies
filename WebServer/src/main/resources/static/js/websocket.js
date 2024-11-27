const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/quizies'
});

stompClient.onConnect = (frame) => {
    console.log('>>> STOMP Client Connected: ' + frame);
    stompClient.subscribe('/topic/gameevent', (gameEventMessage) => {
        gameEvent(JSON.parse(gameEventMessage.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function startGameEvent() {
    stompClient.publish({
        destination: "/app/gameevent",
        body: JSON.stringify({'event': 'StartGame'})
    });
}

function requestQuestionEvent() {
    let questions_categories = notDoneCategories(); // update this to remove done categories
    if (questions_categories.length !== 0) {
        stompClient.publish({
            destination: "/app/gameevent",
            body: JSON.stringify(
                {'event': 'Question',
                    'arguments': questions_categories}
            )
        });
    } else {
        alert("¡¡¡ Juego Terminado !!!")
    }
}

function notDoneCategories() {
    let filtered_score = Object.entries(score).filter(([name, points]) => points < 3);
    let result = filtered_score.map(x => x[0])
    return result;
}

function gameEvent(gameEvent) {
    let eventType = gameEvent.type;
    switch (eventType) {
        case "StartGame":
            categories = gameEvent.categories;
            updateCategory(categories);
            nextQuestionServer();
            break;
        case "Question":
            let question = gameEvent.question
            loadQuestion(question);
            break;
        default:
            console.log("GameEvent -> EventType -> " + eventType + " -> Default case");
    }
}

function nextQuestionServer() {
    // update round
    round++;
    $("#round-number").text(round);

    if ((currentCategory in score) && (score[currentCategory] >= 3)) {
        catIndex = getIndexOfCategory(currentCategory);
        $("#quesito-cat-" + catIndex + " > svg > path").css({ fill: colorsByCategory[currentCategory] })
        //currentCategory = (currentCategory + 1) % 6;
        requestQuestionEvent();
    } else {
        requestQuestionEvent();
    }
}

$(function () {
    connect();
    setTimeout(startGameEvent, 500);
});
