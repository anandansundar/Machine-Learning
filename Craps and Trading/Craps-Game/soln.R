#source("craps.game.R")
balance <- 1000
bet <- 100
games <- 0
while (balance>0 && games<10)
{
  games <- games + 1
  outcome <- craps()
  if (outcome)
  {
    balance <- balance + bet
    bet <- 100
  }
  else
  {
    balance <- balance - bet
    bet <- min(2*bet,balance)
  }
  cat("\nGame: ", games, "Balance Remaining: ", balance)
}

cat("\nFINAL RESULT---Games: ", games, "Balance Remaining: ", balance)
