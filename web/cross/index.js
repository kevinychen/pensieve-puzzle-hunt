import React from 'react';
import ReactDOM from 'react-dom';
import { Wrapper } from "../wrapper";
import "./style.css";

const GRID = `
                                                      []               
                                                      []               
                                                      []               
                                                [] [] 17  5            
                                                      14  1 [] [] [] []
                                                         []            
                                                [] [] [] [] [] [] []   
                                                []       []            
                                       []       []                     
                                       []       []       []            
                              [] [] [] [] [] []  9 11    []            
                                       []       19 12 [] [] [] []      
                                       []          []    []            
                                       []          []    []            
                                    []             []    []            
                                 [] [] [] [] [] [] []                  
                                    []                                 
                        [] [] [] []  4 13                              
                           []       15 16 [] [] []                     
                           []          []                              
                           []       []                                 
                           []       []                                 
                        10  2 [] [] [] []                              
               [] [] [] 18  7       []                                 
                        []          []                                 
                        []                                             
            [] [] [] [] []                                             
               []                                                      
               []                                                      
[] [] [] [] []  6  3                                                   
                8 20 [] [] []                                          
                  []                                                   
                  []                                                   
                  []                                                   
                  []                                                   
                  []                                                   
`

class App extends React.Component {

    render() {
        return (
            <Wrapper
                puzzleId="cross"
                title="King's Cross"
                flavortext="Sunshine, daisies, butter mellow, Turn this stupid, fat rat yellow!"
            >
                <div className="group">
                    <table className="grid">
                        <tbody>
                            {GRID.split('\n').map(rowStr => {
                                if (rowStr !== '') {
                                    return <tr>{this.renderGridRow(rowStr)}</tr>;
                                }
                            })}
                        </tbody>
                    </table>
                    <table className="clues">
                        <tbody>
                            <tr>
                                <td>
                                    <ul>
                                        <li>An umbrella that protects you from pressurized sprays.</li>
                                        <li>A bird's "thumb", made out of a peppery salad green.</li>
                                        <li>A place of worship made out of a light-colored wood.</li>
                                        <li>A quick and skillful robbery.</li>
                                        <li>An eagles nest, taken over by some sprites (alt. spelling).</li>
                                        <li>A California state flower, if left unwatered.</li>
                                        <li>Short and stout, and still steaming.</li>
                                        <li>A rodent found in a residence.</li>
                                        <li>A thermosetting resin with an -OH group.</li>
                                        <li>Refuse to acknowledge the post-performance.</li>
                                        <li>Mass emigration from the state kyc, Michael Xu, and Sumit are from.</li>
                                        <li>A lizard from the largest city in the Baja peninsula.</li>
                                        <li>A pressed sandwich featuring a summer squash.</li>
                                    </ul>
                                </td>
                                <td className="lengths">
                                    <ul>
                                        <li>7 &nbsp; 7</li>
                                        <li>7 &nbsp; 5</li>
                                        <li>5 &nbsp; 6</li>
                                        <li>4 &nbsp; 5</li>
                                        <li>5 &nbsp; 5</li>
                                        <li>6 &nbsp; 5</li>
                                        <li>3 &nbsp; 6</li>
                                        <li>5 &nbsp; 5</li>
                                        <li>7 &nbsp; 5</li>
                                        <li>6 &nbsp; 6</li>
                                        <li>5 &nbsp; 6</li>
                                        <li>7 &nbsp; 6</li>
                                        <li>8 &nbsp; 6</li>
                                    </ul>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <ul>
                                        <li>_ _ _ _ _ _ _ &nbsp; _ _ &nbsp; _ _ _ _ _ _ &nbsp; _ _ &nbsp; _ _ _</li>
                                    </ul>
                                </td>
                                <td className="lengths">
                                    <ul>
                                        <li>7 &nbsp; 5</li>
                                    </ul>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </Wrapper>
        );
    }

    renderGridRow(rowStr) {
        const row = [];
        for (var i = 0; i < rowStr.length; i += 3) {
            const substr = rowStr.substring(i, i + 2);
            if (substr === '[]') {
                row.push(<td className="bordered"></td>)
            } else if (substr === '  ') {
                row.push(<td></td>)
            } else {
                row.push(<td className="bordered">{substr}</td>)
            }
        }
        return row;
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
