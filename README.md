# SOLITAIRE!

This is an implementation of Solitaire that I programmed in my Object-Oriented Design class. It functions almost exactly the same as a normal Solitaire game,
with the exception being that you can view the top two cards of the draw pile at any given point (as displayed at the top of the input screen).

In order to run the game, select the "BasicKlondikeProgram" file and run it. Movements are inputted as follows:

## Moving from the Draw pile to a Foundation
mdf foundation-number
I.E., if I wanted to move from the draw pile to the fourth foundation, I would type "mdf 4".

## Moving from the Draw pile to a hand pile
md pile-number
I.E., if I wanted to move from the draw pile to the third pile, I would type "md 3".

## Moving from pile to pile
mpp pile1-number num-of-cards pile2-number
I.E., if I wanted to move 3 cards from the first pile to the seventh pile, I would type "mpp 1 3 7".

## Moving from pile to a Foundation
mpf pile-number foundation-number
I.E., if I wanted to move from the sixth pile to the second foundation, I would type "mpf 6 2".

## Discarding from the draw pile
dd
This is for when there are no moves to be made except for cycling through the draw pile.

## Qutting the game
q
If you don't want to play anymore, you can exit the game by pressing "q".

## The Rules of the Game

Cards that are face up and showing may be moved from the draw pile or the columns to the
foundation stacks or to other columns.
To move a card to a column, it must be one less in rank and the opposite color. For example, if it
was a 9 of hearts (red), you could put an 8 of spades or clubs onto it. Stacks of cards may be
moved from one column to another as long as they maintain the same order (highest to
lowest, alternating colors).
If you get an empty column, you can start a new column with a King. Any new column must be
started with a King (or a stack of cards that starts with a King).
To get new cards from the draw pile, you turn two cards at a time face up.
You can only play the left-most card of these two cards. Using or discarding this card
automatically cycles through the rest of the cards in the draw pile.
You win once all foundations have full piles, from Ace to King.
