import "../global.css";
import "./style.css";
import React from 'react';
import ReactDOM from 'react-dom';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            guess: '',
            message: undefined,
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
                {this.maybeRenderMessage()}
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
                    if (body.blocked) {
                        alert("You've guessed too many times. Please try again later.");
                    } else if (body.correct) {
                        // TODO can we get Set-Cookie header to work here?
                        document.cookie = "TEAM_XXXXXXX_PUZZLES=" + body.answer;
                        this.setState({ message: body.message });
                    } else {
                        alert("Incorrect");
                    }
                });
            });
        }
    }

    maybeRenderMessage() {
        const { message } = this.state;
        if (!message) {
            return undefined;
        }
        return (
            <div className="message fade-in">
                <div dangerouslySetInnerHTML={{ __html: message }} />
                <a href="/"><button className="continue-button">Continue</button></a>
            </div>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
