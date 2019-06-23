// fetch messages and add them to the page 
// by default, fetch uses GET 
function fetchMessages() {
    const url = '/feed';
    fetch(url).then((response) => {
        return response.json(); // parse the returned json  
    }).then((messages) => {
        const messageContainer = document.getElementById('message-container');
        if (messages.length == 0) {
            messageContainer.innerHTML = '<p>There are no posts yet.</p>';
        } else {
            messageContainer.innerHTML = '';
        }
        messages.forEach((message) => {
            const messageDiv = buildMessageDiv(message);
            messageContainer.appendChild(messageDiv);
        });
    });
}

function buildMessageDiv(message) {

    const wrapper = document.createElement('div'); 
    wrapper.classList.add('card');
    wrapper.style = 'width: 70rem'; 

    const inner_wrapper = document.createElement('div'); 
    inner_wrapper.classList.add('card-body'); 
    wrapper.appendChild(inner_wrapper); 

    const card_title = document.createElement('h4');
    card_title.classList.add('card-title'); 
    card_title.appendChild(document.createTextNode(message.user)); 
    inner_wrapper.appendChild(card_title); 

    const card_list = document.createElement('ul');
    card_list.classList.add('list-group');
    card_list.classList.add('list-group-flush'); 
    wrapper.appendChild(card_list); 

    const time_li = document.createElement('li'); 
    time_li.classList.add('list-group-item'); 
    time_li.appendChild(document.createTextNode(new Date(message.timestamp))); 
    card_list.appendChild(time_li);

    const message_li = document.createElement('li'); 
    message_li.classList.add('list-group-item'); 
    message_li.appendChild(document.createTextNode(message.text)); 
    card_list.appendChild(message_li); 

    return wrapper;
}


// fetch data and populate the UI of the page 
function buildUI() {
    fetchMessages();
}
