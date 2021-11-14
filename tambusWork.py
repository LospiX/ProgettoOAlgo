import sys

nome_file = sys.argv[1]
data = open(nome_file, 'r')

n_vars = int(data.readline())
n_family = int(data.readline())
capacity = int(data.readline())

vars_per_family = [int(val) for val in data.readline().split(' ')[:-2]]
r_cost_family = [int(val) for val in data.readline().split(' ')[:-2]]
cost_per_family = [int(val) for val in data.readline().split(' ')[:-2]]

# cost, r_cost
variables = []
for num_fam_x, fam_dim in enumerate(vars_per_family):
	var_for_fam_x = 0
	while var_for_fam_x < fam_dim:
		cost, r_cost = data.readline().split(' ')
		variables.append((int(cost), int(r_cost), num_fam_x))
		var_for_fam_x += 1

text_output = ''
text_output += 'NAME          ' + str(nome_file) + '\n'
text_output += 'OBJSENSE\n'
text_output += '  MAX\n'
text_output += 'ROWS\n'

# ROWS SECTION
for n, var in enumerate(variables):
	text_output += ' L  ' + 'R_' + str(n) + '\n'
# LAST OF ROW SECTION
text_output += ' L  CAPACITY\n'

# OBJECTIVE FUCTION ROW
text_output += ' N  OBJ\n'  # check if spacing is correct

# WE'RE IGNORING LAZYCONS SECTION CUZ MONKE

# COLUMNS SECTION
text_output += 'COLUMNS\n'
for n, variable in enumerate(variables):
	temp_str = '    COL_'+str(n)
	temp_str += ' '*(14 - len(temp_str)) + 'R_' + str(n)
	temp_str += ' '*(24 - len(temp_str)) + '1'
	temp_str += ' '*(39 - len(temp_str)) + 'CAPACITY'
	temp_str += ' '*(49 - len(temp_str)) + str(variable[0]) + '\n'
	text_output += temp_str

	temp_str = '    COL_'+str(n)
	temp_str += ' '*(14 - len(temp_str)) + 'OBJ'
	temp_str += ' '*(24 - len(temp_str)) + str(variable[1]) + '\n'
	text_output += temp_str

for num_fam_x, fam_dim in enumerate(vars_per_family):
	for num_var, variable in enumerate(variables):
		if variable[2] == num_fam_x:
			temp_str = '    COL_'+str(len(variables) + num_fam_x)
			temp_str += ' '*(14 - len(temp_str)) + 'R_' + str(num_var)
			temp_str += ' '*(24 - len(temp_str)) + '-1' + '\n'
			text_output += temp_str

	temp_str = '    COL_'+str(len(variables) + num_fam_x)
	temp_str += ' '*(14 - len(temp_str)) + 'CAPACITY'
	temp_str += ' '*(24 - len(temp_str)) + str(cost_per_family[num_fam_x])
	temp_str += ' '*(39 - len(temp_str)) + 'OBJ'
	temp_str += ' '*(49 - len(temp_str)) + str(r_cost_family[num_fam_x]) + '\n'

	text_output += temp_str

# RHS SECTION
text_output += '    RHS1      CAPACITY  ' + str(capacity) + '\n'

# BOUND SECTION
text_output += 'BOUNDS\n'
for n, variable in enumerate(variables):
	temp_str = ' BV BND'
	temp_str += ' '*(14 - len(temp_str)) + 'COL_' + str(n)
	temp_str += ' '*(24 - len(temp_str)) + '1' + '\n'
	text_output += temp_str

text_output += 'ENDATA'

f = open("output.txt", "x")
f.write(text_output)
f.close()
data.close()

print(text_output)
