import React from 'react';
import ReactDOM from 'react-dom';

class App extends React.Component {

    render() {
        return (
            <ul>
                <li><a href="blind.html">Blind Lights Out</a></li>
                <li><a href="penultima.html">Penultima</a></li>
                <li><a href="phone.html">Noisy Phone Line</a></li>
                <li><a href="quidditch.html">Quidditch</a></li>
                <li><a href="sorting.html">The Sorting Hat</a></li>
                <li><a href="time.html">The Time Turner</a></li>
                <li><a href="pensieve.html">The Pensieve</a></li>
            </ul>
        )
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
