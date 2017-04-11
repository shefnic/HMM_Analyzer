# Hidden Markov Model Analyzer

## Synopsis

This was a small project to implement the [Viterbi](https://en.wikipedia.org/wiki/Viterbi_algorithm) and [Forward](https://en.wikipedia.org/wiki/Forward_algorithm)
algorithms on a hidden Markov model represented by four matrices, input as comma-separated files.

The CSV-parser itself was an exercise in writing an explicit finite-state machine to read the input files in to memory.

## Execution

The compiled program can be executed with the following command:

java -jar hmm_analyzer.jar -e "emissions file" -v "vector file" -s "states file" -o "observations file" 

### Model File Configurations
All files are read in as comma-separated text

The models are read in to the program where the states and the observations are represented as integers.  State 0 is always the Start always emits the Start observation.

Examples below will abstract the following model:

States:

Start

Rainy

Sunny


Observed emissions:

Start

Walked

Shopped

Cleaned

Walked


#### State file
A list, separated by newlines, of all of the states represented as integers.

e.g.

Start

Rainy

Sunny

is represented as:


0

1

2


#### Observations file
A list, separated by newlines, of all of the observed emissions from the model. As state 0 always emits Start, the beginning of the list must begin with 0.

e.g.

Start

1st observed emission Walked

2nd observed emission Shopped

3rd observed emission Cleaned

4th observed emission Walked

is represented as:


0

1

2

3

2



#### Emissions file
Populate the first row and column with 0's
Remaining cells are populated with a numeric probability in the position (x,y) where State x emits y.

e.g.

(1,1) == rainy: walked

(1,2) == rainy: shopped

(1,3) == rainy: cleaned

(2,1) == sunny: walked

(2,2) == sunny: shopped

(2,3) == sunny: cleaned

is represented as:



0,0,0,0

0,0.1,0.4,0.5

0,0.6,0.3,0.1


#### Vector file
Each cell is populated with a numeric probability in the position (x,y) where State x transitions to State y.
The first column should be all 0's as no State transitions to Start.

e.g.

(0,1) == start -> rainy

(0,2) == start -> sunny

(1,1) == rainy -> rainy

(1,2) == rainy -> sunny

(2,1) == sunny -> rainy

(2,2) == sunny -> sunny

is represented as:



0,0.6,0.4

0,0.7,0.3

0,0.4,0.6
