import "./style.css";
import React from 'react';
import ReactDOM from 'react-dom';
import { createGlobalStyle } from 'styled-components';

import GlennSansBold from "./BwGlennSans-Bold.otf";
import GlennSansMedium from "./BwGlennSans-Medium.otf";
const GlobalStyle = createGlobalStyle`
    @font-face {
        font-family: 'GlennSansBold';
        src: url('${GlennSansBold}');
    }

    @font-face {
        font-family: 'GlennSans';
        src: url('${GlennSansMedium}');
    }
`;

class Puzzle extends React.Component {

    render() {
        const { content, isLast } = this.props;
        return (
            <div>
                <div className="puzzle">
                    <div>{content}</div>
                    <div className="box"></div>
                </div>
                {isLast ? undefined : <hr className="line"></hr>}
            </div>
        );
    }
}

import Image1 from "./image1.png";
import Image2 from "./image2.png";
import Song5 from "./song5.mp3";
import Image5 from "./image5.png";

class App extends React.Component {

    render() {
        return (
            <div>
                <GlobalStyle />
                <h2>The Time Turner</h2>
                <h3>solved by <b>Team Runpeng</b></h3>
                <hr></hr>
                <p className="flavor-text">Time to do this again!</p>

                <Puzzle
                    content={<img src={Image1} />}
                />
                <Puzzle
                    content={<img className="white" src={Image2} />}
                />
                <Puzzle
                    content={<pre className="black">k7/pp1PPPP1/4nn1R/2p5/1qP5/8/2B1b3/2K5 w - -</pre>}
                />
                <Puzzle
                    content={
                        <div className="white">
                            <pre className="align-left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e = 2915<br /><br />
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;N = 1119074311891796995289754233565191872847433722288682636047<br /><br />
                                ciphertext = 1089728221641517441027889073471513892274150231803034840285
                            </pre>
                        </div>
                    }
                />
                <Puzzle
                    content={
                        <div>
                            <div className="block"><a href={Song5}>A melody</a></div>
                            <div><img src={Image5} /></div>
                        </div>
                    }
                />
            </div>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));