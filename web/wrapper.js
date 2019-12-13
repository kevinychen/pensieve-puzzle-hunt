import "./global.css";
import React from 'react';

export class Wrapper extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            guess: '',
            answer: undefined,
        }
    }

    componentDidMount() {
        const { puzzleId } = this.props;
        fetch('/api/guess/solved', {
            method: 'GET',
        }).then(response => {
            response.json().then(body => {
                if (body[puzzleId]) {
                    this.setState({ answer: body[puzzleId] });
                }
            });
        });
    }

    render() {
        const { children, title, flavortext } = this.props;
        return (
            <div>
                <div className="puzzle">
                    <div className="header">
                        <h1>{title}</h1>
                        <span><i>{flavortext}</i></span>
                    </div>
                    {children}
                </div>
                <div className="submit-answer">
                    {this.renderSubmitAnswer()}
                </div>
            </div>
        );
    }

    renderSubmitAnswer() {
        const { guess, answer } = this.state;
        if (answer) {
            return <span className="solved">Solved: {answer}</span>;
        } else {
            return (
                <div>
                    <span className="answer-label">Check answer:&nbsp;</span>
                    <input
                        type="text"
                        onChange={e => this.setState({ guess: e.target.value.toUpperCase().replace(/[^A-Z ]/, '') })}
                        onKeyPress={this.onKeyPress}
                        value={guess}
                    />
                </div>
            );
        }
    }

    onKeyPress = e => {
        const { puzzleId } = this.props;
        const { guess } = this.state;
        if (guess && e.key === 'Enter') {
            fetch('/api/guess', {
                method: 'POST',
                body: JSON.stringify({
                    puzzle: puzzleId,
                    guess,
                }),
                headers: { 'Content-type': 'application/json' },
                credentials: 'include',
            }).then(response => {
                response.json().then(body => {
                    if (body.blocked) {
                        alert("You've guessed too many times. Please try again later.");
                    } else if (body.correct) {
                        this.setState({ answer: body.answer });
                    } else {
                        alert("Incorrect");
                    }
                });
            });
        }
    }
}
