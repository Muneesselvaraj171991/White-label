# Created weather app for white-labelling concept.

I am asked to do the assignment for Weather app using white-label concept. I am playing with product flavours to do white labelling on my app, for example I am expecting 
jsonUI from flavours(see flavour1/res & flavour2/res), If I receive a valid json file I construct view out of fit such as theme colors, text size and Banners position etc.
If you send me an empty json file I consider that as default and forcing app to use default resources.

For this application , I have made three product flavours namely default, flavour1 and flavour2 with different white label resources


I was free to define my own Json api to control dynamic UIs, so 

     {
       "theme": {
                  "light" : { }
                  "dark" : {}
                }
                
        "mainScreen": { "bgDrawables":{} // it supports both res drwable and also url from user
                        "iconDrawablse": {} 
                        "Banners":[current_weather, day_prediction, prediction_list] // user can rearrange the banners based on their needs
                      }
      }
