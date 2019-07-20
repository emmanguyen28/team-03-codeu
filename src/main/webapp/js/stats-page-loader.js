    // Fetch stats and display them in the page.
    function fetchStats(){
      const url = '/stats';
      fetch(url).then((response) => {
        return response.json();
      }).then((stats) => {
        const statsContainer = document.getElementById('stats-container');
        statsContainer.innerHTML = '';

        const messageCountElement = buildStatElement('Message count: ', stats.messageCount);
        const avgMsgLengthElement = buildStatElement('Average message length: ', + stats.averageMessageLength);
        statsContainer.appendChild(messageCountElement);
        statsContainer.appendChild(avgMsgLengthElement);
      });
    }

    function buildStatElement(statString, statBadge){
     const statElement = document.createElement('h3');
     statElement.appendChild(document.createTextNode(statString))
     const statSpan = document.createElement('span'); 
     statSpan.setAttribute("class", "badge badge-info");
     statSpan.appendChild(document.createTextNode(statBadge));
     statElement.appendChild(statSpan);
     return statElement;
    }

    // Fetch data and populate the UI of the page.
    function buildUI(){
     fetchStats();
    }