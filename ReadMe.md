# Advent of Code 2023 in Kotlin
## Setup
- Clone this repository from https://github.com/martinweu/aoc2023/releases/tag/Skeleton 
- Open it up with IntelliJ and make sure gradle is initialized.

## How to use
- Use the two test cases in the Setup-Test to set up the day specified as `DAY_TO_SETUP`
- Use the two test cases in the AOC to run the day and part specified as `PUZZLE` in `Main.kt`
- IntelliJ Run-Configurations for all four of the above actions are supplied

## What it can do
- provides basic setup for running your Kotlin code against the example and your input
- set up your solution file.
- set up **empty** files for example input and provided solution. 
- download your input (you need to supply the sessionid in a file called sessionid inside of the projects root folder)
- keeps track of your generated solutions in the respective out files

## Files
- your solution for day 1 will be in classes called `Day01Part1` for part1 and `Day01Part2` for part2 
- the daily puzzle input/outputs are in the puzzle folder
- each day has its own folder named with the two digit day number
- input for both parts of each advent of code day usually are the same, so there are `example.in` and `my.in`
  -  if they are not you can just create an `example.in2` or `my.in2` which would be picked up instead. 
- you need to copy the example input into `example.in` yourself. if you supplied the sessionid `my.in` will be downloaded by the setup 
- the provided solutions for the examples go into `example.check1` for part1 and `example.check2` respectively
- outputs are added to the `my/example.out1` for part1 and `my/example.out2`

