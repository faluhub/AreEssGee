# AreEssGee

The best cheating mod for MCSR.

## How to detect:

Look at F3 screen, it will say `CHEATING!` on the right side.

# Config

The config file can be found at `.minecraft/config/AreEssGee.json`.

You can add a `file` property to the json file, with its value being a path to another config file to use global configs.
This is helpful for if you have multiple instances, and you don't want to copy the file to every instance every time you want to change anything in the config file.

```
ANTI_BASALT_REGION_SIZE
Description: The amount of chunks (centered around 0, 0) that can't be a Basalt Delta biome.
Default: 120
Vanilla: 0
```

```
ANTI_BASALT_REPLACEMENT
Description: The biome that the Basalt Delta biome gets replaced with. (See ANTI_BASALT_REGION_SIZE)
Default: nether_wastes
Vanilla: basalt_deltas
```

```
AFFECTED_STRUCTURES
Description: List of structures that are affected by AFFECTED_STRUCTURE_RANDOM_OFFSET and AFFECTED_STRUCTURE_RANDOM_BOUND.
Default: ["bastion_remnant", "fortress"]
Vanilla: []
```

```
AFFECTED_STRUCTURE_RANDOM_OFFSET
Description: The guaranteed amount of chunks that the affected structure will be generated away from 0, 0.
Default: 3
Vanilla: 0
```

```
AFFECTED_STRUCTURE_RANDOM_BOUND
Description: The highest number of chunks that can be added onto AFFECTED_STRUCTURE_RANDOM_OFFSET.
Default: 4
Vanilla: 0
```

```
MAX_GLOBAL_STRONGHOLD_ROOM_LIMIT
Description: The max limit of every stronghold room. This does not mean that the stronghold will be x rooms big. (Put any negative number for vanilla)
Default: 10
Vanilla: -1
```

```
FLINT_MINIMUM_VALUE
Description: The minimum value the random number from 0 to 1 has to be to make a gravel block drop flint. (Lower = better)
Default: 0.6
Vanilla: 0.9
```

```
ROD_MINIMUM_VALUE
Description: The minimum value the random number from 0 to 1 has to be to make a blaze drop a blaze rod. (Lower = better)
Default: 0.4
Vanilla: 0.5
```

```
BURIED_TREASURE_RARITY
Description: The value that Minecraft uses for buried treasure rarity. (Higher = better)
Default: 0.07
Vanilla: 0.01
```

```
OCEAN_CANYON_RARITY
Description: The value that Minecraft uses for ocean canyon (ocean ravine) rarity. (Higher = better)
Default: 0.04
Vanilla: 0.02
```

```
TREASURE_BASTION_GOLD_BLOCK_RARITY
Description: The value that Minecraft uses for choosing which gold blocks get generated in the treasure ramparts. (Lower = better)
Default: 0.2
Vanilla: 0.3
```

```
EYE_ODDS
Description: The chance of an eye generating in an end portal frame. (Lower = better)
Default: 0.8
Vanilla: 0.9
```

```
GUARANTEE_EYE_DROPS
Description: Whether or not eye of enders always drop their item after throwing one.
Default: true
Vanilla: false
```

```
ADD_BASALT_BASTIONS
Description: Whether or not bastions can generate in Basalt Delta biomes.
Default: true
Vanilla: false
```
