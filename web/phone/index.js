import "./style.css";
import React from 'react';
import ReactDOM from 'react-dom';
import { Wrapper } from "../wrapper";

class App extends React.Component {

    render() {
        return (
            <Wrapper
                puzzleId="phone"
                title="Wizard's Wireless"
                flavortext="On the table is a phone with several voicemail messages. Unfortunately, the phone line seems to be a bit noisy, and the messages are all cut off."
            >
                <div className="group"> 
                    <h2>Sent</h2>
                    <table className="normal-text">
                        <tbody>
                            <tr>
                                <td>
                                    <ul>
                                        <li>Father of the piano (8)</li>
                                        <li>Game company (8)</li>
                                        <li>General in the 82nd Airborne Division (8)</li>
                                        <li>Supplying (7)</li>
                                        <li>The act of watching something, or failure to watch something? (9)</li>
                                        <li>Usmanov or Lisin, for example (8)</li>
                                    </ul>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div className="group"> 
                    <h2>Received</h2>
                    <table className="normal-text">
                        <tbody>
                            <tr>
                                <td>
                                    <ul>
                                        <li>A drinking boot, in Australia (5)</li>
                                        <li>Beautiful palace (10)</li>
                                        <li>Grief (6)</li>
                                        <li>Have as one's objective (6)</li>
                                        <li>Ron Weasley thought this was embarassing (4)</li>
                                        <li>To flirt in Spanish (5)</li>
                                    </ul>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </Wrapper>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
