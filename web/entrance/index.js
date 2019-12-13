import "../global.css";
import "./style.css";
import * as classNames from "classnames";
import React from 'react';
import ReactDOM from 'react-dom';

import Door from './door.png';
import Werewolf from './werewolf.jpg';
import Sign from './sign.png';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            showPuzzle: false,
            guess: '',
            message: undefined,
        };
    }

    render() {
        const { showPuzzle, guess } = this.state;
        return (
            <div>
                {this.maybeRenderPuzzlePart1()}
                <div
                    className={classNames("door", {"clickable": !showPuzzle})}
                    onClick={() => this.setState({ showPuzzle: true })}
                >
                    <img src={Door} />
                </div>
                {this.maybeRenderPuzzlePart2()}
                <div className="password-label">Password:</div>
                <div className="password-input">
                    <input
                        type="text"
                        onChange={e => this.setState({ guess: e.target.value.toUpperCase().replace(/[^A-Z ]/, '') })}
                        onKeyPress={this.onKeyPress}
                        value={guess}
                        placeholder={"Type here"}
                    />
                </div>
                {this.maybeRenderMessage()}
            </div>
        );
    }

    maybeRenderPuzzlePart1() {
        if (!this.state.showPuzzle) {
            return undefined;
        }
        return (
            <div className="slide-in">
                <div className="werewolf-header"><img src={Werewolf} /></div>
                <div className="werewolf-text this-intro-text-is-not-a-puzzle">
                    <h2>A new day breaks.</h2>
                    <p>
                        After a night of peaceful dreams, you wake to some disturbing news...<br />
                        You have completely forgotten who and where you are.
                    </p>
                    <p>
                        You are in a small prison cell, with walls of stone surrounding you on all sides.
                        The only exit is a door with no knob or handle.
                        You hesitantly push it, but it doesn't budge.
                        Instead, a deep, mechanical voice booms out from the door itself:
                    </p>
                    <i className="boom">PASSWORD????</i>
                </div>
            </div>
        );
    }

    maybeRenderPuzzlePart2() {
        if (!this.state.showPuzzle) {
            return undefined;
        }
        return (
            <div className="slide-in">
                <div className="werewolf-text">
                    <p>Bewildered, you notice a small sign next to the wall.</p>
                </div>
                <div className="werewolf-sign"><img src={Sign} /></div>
                <div className="werewolf-text">
                    <p>Huh, this doesn't look familiar??</p>
                </div>
                <table className="werewolf-clues">
                    <tbody>
                        <tr>
                            <td>561, for example</td>
                            <td>◯ ◯ ◯ ◯ ◯ ◯ ◯ ◯ <span className="hl">◯</span> ◯ &nbsp; ◯ ◯ ◯ ◯ ◯ ◯ </td>
                        </tr>
                        <tr>
                            <td>Brief persuasive speech</td>
                            <td>◯ ◯ ◯ ◯ ◯ ◯ ◯ ◯ &nbsp; <span className="hl">◯</span> ◯ ◯ ◯ ◯</td>
                        </tr>
                        <tr>
                            <td>Cereal with marshmallows</td>
                            <td>◯ ◯ <span className="hl">◯</span> ◯ ◯ &nbsp; ◯ ◯ ◯ ◯ ◯ ◯</td>
                        </tr>
                        <tr>
                            <td>Flows into the Leader River</td>
                            <td>◯ ◯ ◯ ◯ ◯ ◯ ◯  &nbsp; ◯ <span className="hl">◯</span> ◯ ◯ ◯</td>
                        </tr>
                        <tr>
                            <td>"In unbounded numerical data, over a quarter of values will begin with 1"</td>
                            <td>◯ ◯ ◯ ◯ <span className="hl">◯</span> ◯ ◯ ' ◯ &nbsp; ◯ ◯ ◯ </td>
                        </tr>
                        <tr>
                            <td>Nonsense</td>
                            <td>◯ ◯ ◯ ◯ ◯ ◯ <span className="hl">◯</span> ◯ </td>
                        </tr>
                        <tr>
                            <td>Tom's love interest</td>
                            <td>◯ ◯ ◯ ◯ ◯  &nbsp; ◯ <span className="hl">◯</span> ◯ ◯ ◯ ◯ ◯ ◯</td>
                        </tr>
                        <tr>
                            <td>Uncommitted major</td>
                            <td>◯ ◯ ◯ ◯ <span className="hl">◯</span> ◯ ◯ ◯ ◯ ◯</td>
                        </tr>
                    </tbody>
                </table>
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
                        window.scrollTo(0, 0);
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
                {ReactDOM.createPortal(<div className="overlay"></div>, document.body)}
            </div>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
