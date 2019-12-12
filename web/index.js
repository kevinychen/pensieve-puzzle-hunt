import React from 'react';
import ReactDOM from 'react-dom';

class App extends React.Component {

    render() {
        return (
            <ul>
                <li><a href="blind.html">Blind Lights Out</a></li>
                <li><a href="penultima.html">Penultima</a></li>
                <li><a href="sorting.html">The Sorting Hat</a></li>
                <li><a href="time.html">The Time Turner</a></li>
            </ul>
        )
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
