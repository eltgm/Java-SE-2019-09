let stompClient = null;

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/list', (usersJson) => addUsers(JSON.parse(usersJson.body)));
    });
};

const createUser = () => stompClient.send("/app/add", {}, JSON.stringify({
    'name': $("#name-input").val(),
    'login': $("#login-input").val(),
    'password': $("#password-input").val()
}));

const addUsers = (messageStr) => {
    let text;
    let itemsProc = 0;
    messageStr.forEach(messageStrKey => {
        itemsProc++;
        text += '<tr><td>' + messageStrKey.id + '</td>\n' +
            '        <td>' + messageStrKey.name + '</td>\n' +
            '        <td>' + messageStrKey.login + '</td>\n' +
            '        <td>' + messageStrKey.password + '</td></tr>';
        if (itemsProc === messageStr.length)
            callback(text);
    });
};

function callback(text) {
    $("#userDataContainer").append(text);
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
};

$(document).ready(function () {
    connect();
    $("#save").click(createUser)
});

