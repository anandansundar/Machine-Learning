# load SIT package
# Install curl
install.packages('curl', repos = 'http://cran.r-project.org')
library(curl)

###############################################################################
# Install Systematic Investor Toolbox (SIT) package
# github.com/systematicinvestor/SIT
###############################################################################

# please first install SIT.date
devtools::install_github('systematicinvestor/SIT.date')

library(curl)
curl_download('https://github.com/systematicinvestor/SIT/raw/master/SIT.tar.gz', 'sit',mode = 'wb',quiet=T)
install.packages('sit', repos = NULL, type='source')
library(SIT)
