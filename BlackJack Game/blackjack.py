import random

class Card:

    # Card constructor
    # The suit and value of a card, should be immutable.
    def __init__(self, suit: str, value: str):
    
        self._suit= suit
        self._value = value

    # Returns the suit of the card.
    def suit(self) -> str:
        return self._suit
    # Returns the value of the card. 
    def value(self) -> str:
        return self._value
    # Returns a string representation of Card
    # E.g. "Ace of Spades"
    def __str__(self) -> str:
        return "{} of {}".format(self._suit,self._value)


class Deck:
    
    # Creates a sorted deck of playing cards. 13 values, 4 suits.
    # You will iterate over all pairs of suits and values to add them to the deck.
    # Once the deck is initialized, you should prepare it by shuffling it once.
    def __init__(self):
      SUITS = ["Diamonds", "Spades", "Hearts", "Clubs"]
      VALUES = ["Ace", "Two" , "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"]
      self.playing_deck = []
      
      for suit in SUITS:
        for value in VALUES:
          self.playing_deck.append(Card(suit,value))
      
      Deck.shuffle(self)  

    # Returns the number of Cards in the Deck
    def size(self) -> int:
        #size = len(self.playing_deck)
        #print("Deck size: " + str(size))
        return len(self.playing_deck)
    # Shuffles the deck of cards. This means randomzing the order of the cards in the Deck.
    def shuffle(self) -> None:
        random.shuffle(self.playing_deck)
    # Returns the top Card in the deck, but does not modify the deck.
    def peek(self) -> Card:
        #print('Peeking: ')
        return print(self.playing_deck[0])
    # Removes and returns the top card in the deck. The card should no longer be in the Deck.
    def draw(self) -> Card:
        if len(self.playing_deck) != 0:
          return self.playing_deck.pop(0)
        else:
          print("sorry no cards left.\nplease reshuffle.")
        
    # Adds the input card to the deck. 
    # If the deck has more than 52 cards, do not add the card and raise an exception.
    def add_card(self, card: Card) -> None:
        if Deck.size(self) >= 52:
          raise Exception('Deck is full')
        else:
          self.playing_deck.append(Card)
          #print(card)
    # Calling this function should print all the cards in the deck in their current order.
    def print_deck(self) -> str:
      curr_deck = ''
      for self.card in self.playing_deck:
        curr_deck += '\n' + self.card.__str__()
      print('Current Deck: ' + str(curr_deck))
    # Resets the deck to it's original state with all 52 cards.
    # Also shuffle the deck.
    def reset(self) -> None:
        self.__init__()
        self.shuffle()

class Blackjack:
  # Creates a Blackjack game with a new Deck.
  def __init__(self):
    self.play_deck = Deck()
    self.curr_hand = []
    self.dis_hand = []
  # Computes the score of a hand. 
  # For examples of hands and scores as a number.
  # 2,5 -> 7
  # 3, 10 -> 13
  # 5, King -> 15
  # 10, Ace -> 21
  # 10, 8, 4 -> Bust so return -1
  # 9, Jack, Ace -> 20 
  # If the Hand is a bust return -1 (because it always loses)
  def compute_ace(val, ace):
    while val > 21 and ace >=1:
       val -= 10
    return val

    
  def _get_score(self, hand: list[Card]) -> int:

   ace =0
   val_added =0
   face_cards = {'Ten','Jack','Queen','King'}
   for i in range(len(hand)):
    lang = Card.value(hand[i])
    suit = Card.suit(hand[i])
    match lang:
      case 'Ace':
        val_added +=11
        ace +=1
      case 'Two':
        val_added += 2
      case 'Three':
        val_added += 3
      case 'Four':
        val_added +=4
      case 'Five':
        val_added +=5
      case 'Six':
        val_added +=6
      case 'Seven':
        val_added +=7
      case 'Eight':
        val_added +=8
      case 'Nine':
        val_added += 9
      case face_cards:
        val_added += 10
   if val_added >21:
    val_added = Blackjack.compute_ace(val_added,ace)
   if val_added <=21:
    return val_added
   else:
    return -1


  # Prints the current hand and score.
  # E.g. would print out (Ace of Clubs, Jack of Spades, 21)
  # E.g. (Jack of Clubs, 5 of Diamonds, 8 of Hearts, "Bust!")
  def _print_current_hand(self) -> None:
    curr_hand = ''
    score =''
    for self.card in self.curr_hand:
        curr_hand += ' ' + self.card.__str__() +','
    total_val = Blackjack._get_score(self.play_deck,self.curr_hand)

    if total_val >1:
      score = total_val
    else: 
      score = ' Bust!\n'
    
    print('Current Hand: ' + curr_hand + ' ' + str(score) )
    if total_val <1:
      self.curr_hand.clear()
      print("Please deal another hand!\n")

  def _print_dis_deck(self) -> None:
    dis_deck = ''
    for self.card in self.dis_hand:
        dis_deck += ' ' + self.card.__str__() +','
    print('Discarded Deck: ' + dis_deck + ' ') 
    
  # The previous hand is discarded and shuffled back into the deck.
  # Should remove the top 2 cards from the current deck and 
  # Set those 2 cards as the "current hand". 
  # It should also print the current hand and score of that hand.
  # If less than 2 cards are in the deck, 
  # then print an error instructing the client to shuffle the deck.
  def deal_new_hand(self) -> None:
    if len(self.curr_hand) !=0:
      self.dis_hand.append(self.curr_hand)
      self.curr_hand.clear()
    for x in range(2):
      self.curr_hand.append(Deck.draw(self.play_deck))
  

    

  # Deals one more card to the current hand and prints the hand and score.
  # If no cards remain in the deck, print an error.
  def hit(self) -> None:
    if len(self.curr_hand) != 0:

      print("Card drawn: ")
      Deck.peek(self.play_deck)
      self.curr_hand.append(Deck.draw(self.play_deck))
    else:
      raise Exception("please deal a new hand first")
      
      

    
  # Reshuffles all cards in the "current hand" and "discard pile"
  # and shuffles everything back into the Deck.
  def reshuffle(self) -> None:
    self.dis_hand.clear()
    Deck.shuffle(self.play_deck)

print("Welcome, type: \ndeal - to deal a hand \nhit - to draw a card \nend - to end game\nshuffle - to reshufle\ndis- to show discarded deck")
blackjack = Blackjack()

while True:
  op = input( )
  match op:
      case 'deal':
        blackjack.deal_new_hand()
        blackjack._print_current_hand()
      case 'hit':
        blackjack.hit()
        blackjack._print_current_hand()
      case 'end':
        print("thanks for playing! :)")
        exit()
      case 'shuffle':
        blackjack.reshuffle()
        print("Deck has been reshuffled")
      case 'dis':
        blackjack._print_dis_deck()
      case other:
        print("Please use one of the options provided, try again!\n")


'''
-Variables were made private and were accessed using getter methods
- to support more than two players one would implement a player class
that holds a player details plus a players current hand
- adding more decks, will be just to loop through the innit deck method
as many times as whished, and append that new deck to the end of the current one. 

'''