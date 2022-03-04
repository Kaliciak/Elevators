# How to run project
To run this project, first you need to have installed latest version of gradle.\
Then you can run this project with 
```sh
$ gradle run
```
# Instruction
## Main menu
Here you can see three sliders:
* Number of elevators: number of elevators to be simulated - from 1 to 16
* Max floor: the number of the highest floor in the building - from 0 to 10
* Min floor: the number of the lowest floor in the buliding - from -10 to 0

After adjusting the settings you can press the "Simulate" button to start simulation of the system.
## Simulation
On the top you can see two buttons:
* Back: go back and change settings of the simulation
* Step: perform next step of the simulation

On the left side you can see rectangles with number of the floor and two buttons that you can use to request any elevator to come to that floor, with information to what direction do you want to go.
When the button is highlighted, it means that this request is waiting to be realized by any elevator.

To the right of them you can see rectangles representing elevator shafts and the places where the elevators are at the moment. You can press at any rectangle to request specific elevetor to come to that floor.\
Rectangles may have different colors, depending on current requests of the system:
* Red: this floor is the next target of this elevator
* Orange: this floor has been requested to be visited by this elevator

When elevator arrives at the floor that was requested, it opens its doors for one step and then is ready to visit next floor.
