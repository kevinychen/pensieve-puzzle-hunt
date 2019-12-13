import React from 'react';
import ReactDOM from 'react-dom';
import { Wrapper } from "../wrapper";
import "./style.css";

import Player1 from "./player1.jpg";
import Player2 from "./player2.jpg";
import Player3 from "./player3.jpg";
import Player4 from "./player4.jpg";
import Player5 from "./player5.jpg";
import Player6 from "./player6.jpg";
import Player7 from "./player7.jpg";
import Beater1 from "./beater1.mp3";
import Beater2 from "./beater2.mp3";
import Chaser1 from "./chaser1.png";
import Chaser2 from "./chaser2.png";
import Chaser4 from "./chaser4.png";
import Chaser5 from "./chaser5.png";
import Chaser6 from "./chaser6.png";
import Seeker from "./seeker.png";

class App extends React.Component {

    render() {
        return (
            <Wrapper
                puzzleId="quidditch"
                title="Quidditch"
                flavortext="There are seven players on a Quidditch team."
            >
                <div className="group">
                    <div className="players">
                        <img src={Player1} />
                        <img src={Player2} />
                        <img src={Player3} />
                        <img src={Player4} />
                        <img src={Player5} />
                        <img src={Player6} />
                        <img src={Player7} />
                    </div>
                    <div className="player-boxes">
                        <div className="box"></div>
                        <div className="box"></div>
                        <div className="box"></div>
                        <div className="box"></div>
                        <div className="box"></div>
                        <div className="box"></div>
                        <div className="box"></div>
                    </div>
                </div>
                <hr />
                <div className="group beaters">
                    <h2>Beaters</h2>
                    <span><i>Beaters play music. A sound can illuminate ideas.</i></span>
                    <div className="block">
                        <div><a href={Beater1}>Sunshine, 1833</a></div>
                        <div className="box"></div>
                    </div>
                    <div className="block">
                        <div><a href={Beater2}>Gavotte, 1705</a></div>
                        <div className="box"></div>
                    </div>
                </div>
                <hr />
                <div className="group chasers">
                    <h2>Chasers</h2>
                    <span><i>The Lucky Cauldron has been getting uncreative - they use the same ingredients for their
                        chasers as for their regular mixed drinks (just without the alcohol). But they’re still
                        delicious; what’s the secret ingredient?</i></span>
                    <div className="block">
                        <div>
                            <span><img src={Chaser1} /><span className="big">(5)</span></span>
                            <span className="big">+</span>
                            <span><img src={Chaser2} /><span className="big">(6)</span></span>
                            <span className="big">→</span>
                            <div className="box"></div>
                        </div>
                        <div>
                            <span><span className="medium">[A clue elsewhere in this puzzle]</span><span className="big">(5)</span></span>
                            <span className="big">+</span>
                            <span><img src={Chaser4} /><span className="big">(4)</span></span>
                            <span className="big">→</span>
                            <div className="box"></div>
                        </div>
                        <div>
                            <span><img src={Chaser5} /><span className="big">(4)</span></span>
                            <span className="big">+</span>
                            <span><img src={Chaser6} /><span className="big">(4)</span></span>
                            <span className="big">→</span>
                            <div className="box"></div>
                        </div>
                    </div>
                </div>
                <hr />
                <div className="group keeper">
                    <h2>Keeper</h2>
                    <span><i>Find the Keeper of the Clue. The start code is DGDLTC.</i></span>
                    <div className="block"><div className="box"></div></div>
                </div>
                <hr />
                <div className="group seeker">
                    <h2>Seeker</h2>
                    <span><i>Seek <s>Syren</s> the Snitch.</i></span>
                    <div className="block">
                        <img src={Seeker} />
                        <div><div className="box"></div></div>
                    </div>
                </div>
            </Wrapper>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
