### What is this plugin?
It's an extension to CraftingStore that allows server owners to automate the process of generating Gift Cards through CraftingStore's API.
Additionally, it will send an email to the player containing the Gift Card Code through the mail used during purchase.

### What is CraftingStore?
It's a donation platform, built to give all servers (small or big) a chance to receive donations and process them. They offer a very generous bronze (free) plan, that includes all the basics and more to boost your sales to the next level!

### Current Features
- Generate GiftCard
- Send Gift Card Code to player in-game.
- Send an email to the player containing the Gift Card Code through the mail used during purchase.
- HTML Template for the email.

### Setup
1) Download and install the plugin
2) Start server
3) Edit config.yml in newly created GiftCard folder

Additionally, you need to add following two commands to your Gift Card packages on CraftingStore:
```
giftcard givecard x {player}
giftcard emailcard {player} {email}
```
(Replace x with the amount the Gift Card Package is on)
Set to execute when player is online.

### Support
Need help? Add me on Discord: MatiasMunk#6199
