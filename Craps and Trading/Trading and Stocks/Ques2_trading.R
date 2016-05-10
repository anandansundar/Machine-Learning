# install systematic investor
# Run only once
# install.packages('curl', repos = 'http://cran.r-project.org')
###############################################################################
# Install Systematic Investor Toolbox (SIT) package
# github.com/systematicinvestor/SIT
###############################################################################

# please first install SIT.date
#devtools::install_github('systematicinvestor/SIT.date')

#library(curl)
#curl_download('https://github.com/systematicinvestor/SIT/raw/master/SIT.tar.gz', 'sit',mode = 'wb',quiet=T)
#install.packages('sit', repos = NULL, type='source')
#library(SIT)
##########
# test strategies
##########

#load.packages('quantmod')

#*****************************************************************
# Load historical data
#******************************************************************     
load.packages('quantmod')
tickers = spl('PCLN')

data <- new.env()
getSymbols(tickers, src = 'yahoo', from = '2000-01-01', env = data, auto.assign = T)
bt.prep(data, align='keep.all', dates='2000::2015')

#*****************************************************************
# Code Strategies
#****************************************************************** 
prices = data$prices    

# Buy & Hold    
data$weight[] = 1
buy.hold = bt.run(data) 

# MA Cross
sma = bt.apply(data, function(x) { SMA(Cl(x), 200) } )  
data$weight[] = NA
data$weight[] = iif(prices >= sma, 1, 0)
sma.cross = bt.run(data, trade.summary=T)           

#*****************************************************************
# Create Report
#****************************************************************** 
#plotbt.custom.report.part1(buy.hold, sma.cross)
#plotbt.custom.report.part2(buy.hold, sma.cross)
#plotbt.custom.report.part3(buy.hold, sma.cross)
#chartSeries(buy.hold,sma.cross)

getSymbols(tickers, src='yahoo', from ='2000-01-01')
chartSeries(PCLN,theme=chartTheme("black"))
plot(addSMA(200))
rm(data)