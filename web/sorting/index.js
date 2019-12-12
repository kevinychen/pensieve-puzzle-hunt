import "./style.css";
import React from 'react';
import ReactDOM from 'react-dom';
import { Wrapper } from "../wrapper";

class App extends React.Component {

    render() {
        return (
            <Wrapper
                title="The Sorting Hat"
                flavortext="Sort the new students into their houses."
            >
                <div className="group"> 
                    <h2>First years</h2>
                    <table className="first-years">
                        <tr>
                            <td>
                                <ul>
                                    <li>A menmonic for spaces</li>
                                    <li>A sperm whale in The Hitchhiker’s Guide to the Galaxy hits this and dies</li>
                                    <li>A time zone at UTC+05:30</li>
                                    <li>Aves, for example</li>
                                    <li>It’s written on a sign to advise only ~50% of people to pass</li>
                                    <li>Katakana character that looks like the letter <i>K</i></li>
                                    <li>Meeting</li>
                                    <li>Plan or specification</li>
                                    <li>Sci-fi author</li>
                                    <li>Speech sound</li>
                                    <li>The Americas, in the early 16th century</li>
                                    <li>Type of radio</li>
                                </ul>
                            </td>
                            <td>
                                <ul>
                                    <li>A magic effect where a card inserted into the middle of a deck re-appears at the top</li>
                                    <li>Area where circuses are held</li>
                                    <li>Biblical Magi</li>
                                    <li>Dystopian novel</li>
                                    <li>If you land on Jail, you're ___</li>
                                    <li>In modern times, we almost all have one</li>
                                    <li>Not a patriot</li>
                                    <li>One of the families in <i>To Kill a Mockingbird</i></li>
                                    <li>The theory that life and the universe could not have arisen by chance</li>
                                    <li>Type 45 destroyer</li>
                                    <li>Unit for the slope of a titration curve (mmol/pH), named after a chemist</li>
                                    <li><b>What this clue is written in</b></li>
                                </ul>
                            </td>
                        </tr>
                    </table>
                </div>
                <div className="group">
                    <h2>Houses</h2>
                    <table className="houses">
                        <tr>
                            <td>
                                <h3>Gryffindor</h3>
                                <ul className="unstyled">
                                    <li>◯ ① ◯ ◯</li>
                                    <li>◯ ◯ ④ &nbsp;&nbsp; ◯ ◯ ◯ ◯ ◯</li>
                                    <li>◯ ⑫ ◯ ◯ ◯</li>
                                </ul>
                            </td>
                            <td>
                                <h3>Hufflepuff</h3>
                                <ul className="unstyled">
                                    <li>◯ ③ ◯</li>
                                    <li>◯ ⑦ ◯ ◯ ◯ ◯</li>
                                    <li>⑨ ◯ ◯ ◯ ◯ ◯ ◯ ◯</li>
                                </ul>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h3>Ravenclaw</h3>
                                <ul className="unstyled">
                                    <li>◯ ◯ ◯ ② ◯ </li>
                                    <li>◯ ◯ ◯ ⑤</li>
                                    <li>◯ ⑧ ◯ ◯ ◯ ◯</li>
                                </ul>
                            </td>
                            <td>
                                <h3>Slytherin</h3>
                                <ul className="unstyled">
                                    <li>◯ ◯ ⑥ ◯</li>
                                    <li>◯ ⑩</li>
                                    <li>◯ ⑪ ◯</li>
                                </ul>
                            </td>
                        </tr>
                    </table>
                </div>
            </Wrapper>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
