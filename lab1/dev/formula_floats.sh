#!/bin/bash

check_point() {
    local x=$1
    local y=$2
    local R=$3

    # x >= -R && x <= 0 && y <= R/2 && y >= 0
    if (( $(echo "$x >= -$R && $x <= 0 && $y <= $R/2 && $y >= 0" | bc -l) )); then
        return 0
    fi

    # x >= 0 && (y >= x/2 - R/2) && y <= 0
    if (( $(echo "$x >= 0 && $y >= $x/2 - $R/2 && $y <= 0" | bc -l) )); then
        return 0
    fi

    # x^2 + y^2 <= R^2 && x >= 0 && y >= 0
    if (( $(echo "$x >= 0 && $y >= 0" | bc -l) )); then
        local x_squared=$(echo "$x * $x" | bc -l)
        local y_squared=$(echo "$y * $y" | bc -l)
        local r_squared=$(echo "$R * $R" | bc -l)
        if (( $(echo "$x_squared + $y_squared <= $r_squared" | bc -l) )); then
            return 0
        fi
    fi

    return 1
}

if [ -z "$X" ] || [ -z "$Y" ] || [ -z "$R" ]; then
    echo "X,Y,R envvars are not set."
    exit 1
fi

# floats
x=$X
y=$Y
R=$R

if check_point "$x" "$y" "$R"; then
    echo "The point ($x, $y) is in one of the areas."
else
    echo "The point ($x, $y) is not in any of the areas."
fi
