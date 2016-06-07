#!/usr/bin/env python

'''
Method to take two equally-sized lists and return just the elements which lie
on the Pareto frontier, sorted into order.
Default behaviour is to find the maximum for both X and Y, but the option is
available to specify maxX = False or maxY = False to find the minimum for either
or both of the parameters.
'''
import os, sys
import pandas as pd
import matplotlib.pyplot as plt

# TODO: set the input and output folders here
# The analysis result text file
file_name = sys.argv[1]
# The output folder
output_dir = sys.argv[2]

name_only = file_name[:-4]
# The created figures are stored in output_dir/fig
fig_dir_name = os.path.join(output_dir, 'fig')
fig_file = os.path.join(fig_dir_name, name_only)
csv_file = os.path.join(output_dir, name_only+'.csv')


if not os.path.exists(fig_dir_name):
    os.makedirs(fig_dir_name)

# text file to csv file
with open(file_name, 'r') as fp, open(csv_file, 'w') as csv_fp:
    for line in fp:
        segs = line.split(':')
        csv_fp.write(','.join(segs)+'\n')


# define the label size in plot
label_size = 20
plt.rcParams['xtick.labelsize'] = label_size
plt.rcParams['ytick.labelsize'] = label_size

# make room for x and y axis
plt.rcParams.update({'figure.autolayout': True})

#set up the size of the special points
normal_dot_size = 60
special_dot_size = 240

# the width of Pareto-optimal line
linewidth = 2
# normal_dot_size = 15
#configure the figure
marker_size = 15
graph_dpi = 500
inserttime_unit = 'sec'
selecttime_unit = 'sec'
spaceconsumption_unit = 'MB'
lable_font_size = 25


def pareto_frontier(Xs, Ys, maxX = True, maxY = True):
# Sort the list in either ascending or descending order of X
    myList = sorted([[Xs[i], Ys[i]] for i in range(len(Xs))], reverse=maxX)
# Start the Pareto frontier with the first value in the sorted list
    p_front = [myList[0]]
# Loop through the sorted list
    for pair in myList[1:]:
        if maxY:
            if pair[1] >= p_front[-1][1]: # Look for higher values of Y…
                p_front.append(pair) # … and add them to the Pareto frontier
        else:
            if pair[1] <= p_front[-1][1]: # Look for lower values of Y…
                p_front.append(pair) # … and add them to the Pareto frontier
# Turn resulting pairs back into a list of Xs and Ys
    p_frontX = [pair[0] for pair in p_front]
    p_frontY = [pair[1] for pair in p_front]
    return p_frontX, p_frontY

read_data = pd.read_csv(csv_file, header=None, names=['Name', 'InsertTime', 'SelectTime', 'SpaceConsumption'])

 #Plot 2D Pareto Optimal graphs
table1 = read_data[['InsertTime', 'SpaceConsumption']]
table2 = read_data[['InsertTime', 'SelectTime']]
table3 = read_data[['SelectTime', 'SpaceConsumption']]
# get your data from somewhere to go here
Xs = table1.InsertTime/1000         # millisecond to second
Ys = table1.SpaceConsumption/1024   # KB to MB

# Draw Insert-Space consumption figure
# Find lowest values for both
p_front = pareto_frontier(Xs, Ys, maxX = False, maxY = False)
# Plot a scatter graph of all results
plt.figure()
plt.scatter(Xs, Ys, s=normal_dot_size, color='black', label="All Generated Solutions")
# Then plot the Pareto frontier on top
plt.plot(p_front[0], p_front[1], mfc='white', linewidth=linewidth, color='black', markeredgewidth=1.5, marker='^', markersize=marker_size, label='Pareto Frontier')
plt.xlabel('Insert Time' + ' (' + inserttime_unit + ')', fontsize=lable_font_size)
plt.ylabel('Space Consumption'+ ' (' + spaceconsumption_unit + ')', fontsize=lable_font_size)
plt.title(name_only+' Insert-Space Tradeoff', fontsize=20)
plt.savefig(fig_file + '_ISpace', dpi=graph_dpi)

# get your data from somewhere to go here
Xs = table2.InsertTime/1000
Ys = table2.SelectTime/1000

# Draw Insert-Select consumption figure
# Find lowest values for both
p_front = pareto_frontier(Xs, Ys, maxX = False, maxY = False)
# Plot a scatter graph of all results
plt.figure()
plt.scatter(Xs, Ys, s=normal_dot_size, color='black', label="All Generated Solutions")
# Then plot the Pareto frontier on top
plt.plot(p_front[0], p_front[1], mfc='white', linewidth=linewidth, color='black', markeredgewidth=1.5, marker='^', markersize=marker_size, label='Pareto Frontier')
plt.xlabel('Insert Time' + ' (' + inserttime_unit + ')', fontsize=lable_font_size)
plt.ylabel('Select Time'+ ' (' + selecttime_unit + ')', fontsize=lable_font_size)
plt.title(name_only +' Insert-Select Tradeoff', fontsize=20)
plt.savefig(fig_file + '_ISelect', dpi=graph_dpi)

# get your data from somewhere to go here
Xs = table3.SelectTime/1000
Ys = table3.SpaceConsumption/1024

# Draw Select-Space consumption figure
# Find lowest values for both
p_front = pareto_frontier(Xs, Ys, maxX = False, maxY = False)
# Plot a scatter graph of all results
plt.figure()
plt.scatter(Xs, Ys, s=normal_dot_size, color='black', label="All Generated Solutions")
# Then plot the Pareto frontier on top
plt.plot(p_front[0], p_front[1], mfc='white', linewidth=linewidth, color='black', markeredgewidth=1.5, marker='^', markersize=marker_size, label='Pareto Frontier')
plt.xlabel('Select Time'+ ' (' + selecttime_unit + ')', fontsize=lable_font_size)
plt.ylabel('Space Consumption'+ ' (' + spaceconsumption_unit + ')', fontsize=lable_font_size)
plt.title(name_only + ' Select-Space Tradeoff', fontsize=20)
plt.savefig(fig_file + '_SelectSpace', dpi=graph_dpi)

print('Done!')
