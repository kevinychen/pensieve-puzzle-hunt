import "./style.css";
import React from 'react';
import ReactDOM from 'react-dom';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            guess: '',
        };
    }

    render() {
        const { guess } = this.state;
        return (
            <div>
                <div className="door"></div>
                <div className="password-label">Password:</div>
                <div className="password-input">
                    <input
                        type="text"
                        onChange={e => this.setState({ guess: e.target.value.toUpperCase().replace(/[^A-Z ]/, '') })}
                        onKeyPress={this.onKeyPress}
                        value={guess}
                    />
                </div>
            </div>
        );
    }

    onKeyPress = e => {
        const { guess } = this.state;
        if (guess && e.key === 'Enter') {
            fetch('/api/guess/enter', {
                method: 'POST',
                body: JSON.stringify({
                    guess,
                }),
                headers: { 'Content-type': 'application/json' },
                credentials: 'include',
            }).then(response => {
                response.json().then(body => {
                    if (body.correct) {
                        // TODO can we get Set-Cookie header to work here?
                        document.cookie = "TEAM_XXXXXXX_PUZZLES=" + body.answer;
                        window.location = '/';
                    } else {
                        alert("Incorrect");
                    }
                });
            });
        }
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
