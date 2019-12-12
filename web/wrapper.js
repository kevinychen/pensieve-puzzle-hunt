import "./style.css";
import React from 'react';

export class Wrapper extends React.Component {

    render() {
        const { children, title, flavortext } = this.props;
        return (
            <div>
                <div className="header">
                    <h1>{title}</h1>
                    <span><i>{flavortext}</i></span>
                </div>
                {children}
            </div>
        );
    }
}
