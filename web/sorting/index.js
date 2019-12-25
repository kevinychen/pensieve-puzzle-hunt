import "./style.css";
import React from 'react';
import ReactDOM from 'react-dom';
import { Wrapper } from "../wrapper";

class App extends React.Component {

    render() {
        return (
            <Wrapper
                puzzleId="sorting"
                title="The Sorting Hat"
                flavortext="Sort the new students into their houses."
            >
                <div className="group"> 
                    <h2>New students</h2>
                    <table className="first-years normal-text">
                        <tbody>
                            <tr>
                                <td>
                                    <ul>
                                        <li>ケ</li>
                                        <li>A mnemonic for the spaces on a staff</li>
                                        <li>A sperm whale in <i>The Hitchhiker’s Guide to the Galaxy</i> hits this and dies</li>
                                        <li>A time zone at UTC+05:30</li>
                                        <li>Aves, for example</li>
                                        <li>It’s written on a sign to advise only ~50% of people to pass</li>
                                        <li>Meeting</li>
                                        <li>Plan or specification</li>
                                        <li>Sci-fi author</li>
                                        <li>Speech sound</li>
                                        <li>The Americas</li>
                                        <li>Type of radio</li>
                                    </ul>
                                </td>
                                <td>
                                    <h4>Also known as...</h4>
                                    <ul>
                                        <li>A magic effect where a card inserted into the middle of a deck re-appears at the top (9 4)</li>
                                        <li>Area where circuses, shows, and other outdoor festivities are held (10)</li>
                                        <li>Biblical Magi (4 3)</li>
                                        <li>Dystopian novel (5 3 5)</li>
                                        <li>In modern times, we almost all have one (10)</li>
                                        <li>Not a patriot (8)</li>
                                        <li>One of the families in <i>To Kill a Mockingbird</i> (10)</li>
                                        <li>Phrase written on the In Jail square (4 8)</li>
                                        <li>The theory that life and the universe could not have arisen by chance (11 6)</li>
                                        <li>Type 45 destroyer (6 5)</li>
                                        <li>Unit for the slope of a titration curve (mmol/pH), named after a chemist (5)</li>
                                        <li><b>What this clue is written in (8)</b></li>
                                    </ul>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div className="group">
                    <h2>Houses</h2>
                    <table className="houses normal-text">
                        <tbody>
                            <tr>
                                <td>
                                    <h3>Gryffindor</h3>
                                    <ul>
                                        <li>◯ ① ◯ ◯</li>
                                        <li>◯ ◯ ④ &nbsp;&nbsp; ◯ ◯ ◯ ◯ ◯</li>
                                        <li>◯ ⑫ ◯ ◯ ◯</li>
                                    </ul>
                                </td>
                                <td>
                                    <h3>Hufflepuff</h3>
                                    <ul>
                                        <li>◯ ③ ◯</li>
                                        <li>◯ ⑦ ◯ ◯ ◯ ◯</li>
                                        <li>⑨ ◯ ◯ ◯ ◯ ◯ ◯ ◯</li>
                                    </ul>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <h3>Ravenclaw</h3>
                                    <ul>
                                        <li>◯ ◯ ◯ ② ◯ </li>
                                        <li>◯ ⑤ ◯</li>
                                        <li>◯ ⑧ ◯ ◯ ◯ ◯</li>
                                    </ul>
                                </td>
                                <td>
                                    <h3>Slytherin</h3>
                                    <ul>
                                        <li>◯ ◯ ⑥ ◯</li>
                                        <li>◯ ⑩</li>
                                        <li>◯ ⑪ ◯</li>
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
