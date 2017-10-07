# Ski-Compass
MSE MobOp Mini Project

## Rest API

### 1. [Open Snow](https://opensnow.com/about/api)
- List of Skiresorts
- Weather forecast
- need a api key -> found on internet wihtout registration
- example list all Locations in Canton Graubünden http://opensnow.com/api/public/1.0/locations/?apikey={apikey}&state=CH-GR
- example show weather forecast in Skiresort Arosa http://opensnow.com/api/public/1.0/locations/data?apikey={apikey}&lids=1832&type=json

### 2. [Skimap.org](https://skimap.org/pages/Developers)
- List of Skiresorts and multiple Ski Maps
- Location
- Link to official Website
- Operation Status
- example list all Locations in Canton Graubünden https://skimap.org/Regions/view/383.xml
- example show informations abaout Skiresort Arosa https://skimap.org/SkiAreas/view/1017.xml
- Number of Skiresorts:10 Ostschweiz, 13 Berneroberland,  16 Innerschweiz, 27 Graubünden, 7 Tessin, 48 Wallis -> Total: 121

### 3. [snowcountry.net](https://feeds.snocountry.net/)
- List of Skiresorts
- Weather forecast
- actual Snow
- Opened Lifts
- Opened Slopes
- Extra Info linke Website, Facebook, Twitter, etc.
- Location
- need a api key -> Testing with key SnoCountry.example
- example list all Locations in Switzerland 
- example show information about Skiresort Arosa http://feeds.snocountry.net/conditions.php?apiKey=SnoCountry.example&ids=13003
- only 51 Skiresorts in Switzerland
