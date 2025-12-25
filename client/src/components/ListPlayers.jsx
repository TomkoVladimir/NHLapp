import React, { useEffect, useState } from 'react'
import { listPlayers } from '../services/PlayerService';

export const ListPlayers = () => {

    const [players, setPlayers] = useState([]);

    useEffect(() => {
        listPlayers().then((response) => {
            setPlayers(response.data)
            console.log(response.data)
        }).catch(error => {
            console.error(error)
        })
    }, [])


    return (
        <>
        
            <h2 className="text-center">List of players</h2>
            <table>
                <thead>
                    <tr>
                    <th>Nick Names</th>
                    </tr>
                    
                </thead>
                <tbody>
                    {
                        players.map(player =>
                            <tr key={player.nickName}>
                                <td>{player.nickName}</td>
                            </tr>)
                    }
                </tbody>
            </table>
        </>
    )
}
