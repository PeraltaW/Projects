#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

typedef struct Gate {
    char gate_type[17];
    int num;
    int s;
    int *sel;
    int *input;
    int *output;
}Gate;


void AND(int *vals, int in_index1, int in_index2, int out_index) {
    vals[out_index] = vals[in_index1] && vals[in_index2];
}

void OR(int *vals, int in_index1, int in_index2, int out_index) 
{
    vals[out_index] = vals[in_index1] || vals[in_index2];
}
void NOT(int *vals, int in_index, int out_index) 
{
    vals[out_index] = !vals[in_index];
}

void NAND(int *vals, int in_index1, int in_index2, int out_index) 
{
    vals[out_index] = !(vals[in_index1] && vals[in_index2]);
}

void NOR(int *vals, int in_index1, int in_index2, int out_index) {
    vals[out_index] = !(vals[in_index1] || vals[in_index2]);
}

void XOR(int *vals, int in_index1, int in_index2, int out_index) {
    vals[out_index] = vals[in_index1] ^ vals[in_index2];
}

void MULTIX(int *vals, int n, int *in_index, int *s_index, int out_index) 
{
    int s = 0;
    for (int i = 0; i < n; i++) {
     s += vals[s_index[i]] * pow(2, n - i - 1);
    }
    vals[out_index] = vals[in_index[s]];
}

void DECODER(int *vals, int num, int *in_index, int *out_index) 
{
    int s = 0;
    for (int i = 0; i < pow(2, num); i++) {
        vals[out_index[i]] = 0;
    }
    for (int i = 0; i < num; i++) {
        s += vals[in_index[i]] * pow(2, num - i - 1);
    }vals[out_index[s]] = 1;
}

int indexOf(int size, char **arr, char *var) 
{
    for (int i = 0; i < size; i++) {
        if (strcmp(arr[i], var) == 0) {
            return i;
        }
    }
    return -1;
}

int increment_input(int *arr, int input_count) 
{
    for (int i = input_count + 1; i >= 2; i--) {
        arr[i] = !arr[i];
        if (arr[i] == 1) {
            return 1;
        }
    }
    return 0;
}

void val_reset(int size, int *arr) {

    for (int i = 0; i < size; i++) {
        arr[i] = 0;
    }
    arr[1] = 1;
}

int main(int argc, char* argv[]) 
{

    if (argc - 1 != 1) {
        perror("Invalid argument number =(\n");
        return 0;
    }
    FILE *fptr = fopen(argv[1], "r");
    if (!fptr) {
        perror("Invalid File\n");
        return 0;
    }
    char dir[17];
    Gate* steps = NULL;
    int step_count = 0;
    int size = 2;
    int input_count = 0;
    int output_count = 0;
    int tcount = 0;
    char **names;
    int *vals = malloc(sizeof(int));

    fscanf(fptr, " %s", dir);
    fscanf(fptr, "%d", &input_count);

    size += input_count;
    names = malloc(size * sizeof(char*));
    names[0] = malloc(1 * sizeof(char));
    names[0] = "0";
    names[1] = malloc(1 * sizeof(char));
    names[1] = "1";

    int i;
    for (i = 0; i < input_count; i++) {
        names[i + 2] = malloc(17 * sizeof(char));
        fscanf(fptr, "%*[: ]%16s", names[i + 2]);
    }
    fscanf(fptr, " %s", dir);
    fscanf(fptr, "%d", &output_count);
    size += output_count;
    names = realloc(names, size * sizeof(char *));
    for (i = 0; i < output_count; i++) {
        names[i + input_count + 2] = malloc(17 * sizeof(char));
        fscanf(fptr, "%*[: ]%16s", names[i + input_count + 2]);
    }
    while (!feof(fptr)) {
        int num_Inputs = 2, num_Outputs = 1;
        Gate ops;
        int sc = fscanf(fptr, " %s", dir); 
        if (sc != 1) {
            break;
        }
        step_count++;
        ops.num = 0;
        ops.s = 0;
        strcpy(ops.gate_type, dir);
        if (strcmp(dir, "DECODER") == 0) {
            fscanf(fptr, "%d", &num_Inputs);
            ops.num = num_Inputs;
            num_Outputs = pow(2, num_Inputs);
        }
        if (strcmp(dir, "MULTIPLEXER") == 0) {
            fscanf(fptr, "%d", &num_Inputs);
            ops.s = num_Inputs;
            num_Inputs = pow(2, num_Inputs);
        }

       if (strcmp(dir, "NOT") == 0) {
            num_Inputs = 1;
        }
        ops.input = malloc(num_Inputs * sizeof(int));
        ops.output = malloc(num_Outputs * sizeof(int));
        ops.sel = malloc(ops.s * sizeof(int));

        char var[17];
        for (i = 0; i < num_Inputs; i++) {
            fscanf(fptr, "%*[: ]%16s", var);
            ops.input[i] = indexOf(size, names, var);
        }

        for (i = 0; i < ops.s; i++) {
            fscanf(fptr, "%*[: ]%16s", var);
            ops.sel[i] = indexOf(size, names, var);
        }
 
        for (i = 0; i < num_Outputs; i++) {
            fscanf(fptr, "%*[: ]%16s", var);
            int idx = indexOf(size, names, var);
            if (idx == -1) {
                size++;
                tcount++;
                names = realloc(names, size * sizeof(char *));
                names[size - 1] = malloc(17 * sizeof(char));
                strcpy(names[size - 1], var);
                ops.output[i] = size - 1;
            }
            else {
                ops.output[i] = idx;
            }
        }
        
        if (!steps) {
            steps = malloc(sizeof(Gate));
        } else {
            steps = realloc(steps, step_count * sizeof(Gate));
        }
        steps[step_count - 1] = ops;
    }
    vals = malloc(size * sizeof(int));
    val_reset(size, vals);

while(1) 
{
        for (i = 0; i < input_count; i++) 
	{
            printf("%d ", vals[i + 2]);
        }
        printf("|");

for (i = 0; i < step_count; i++) 
{
            Gate ops = steps[i];
        if (strcmp(ops.gate_type, "AND") == 0) 
	{
         AND(vals, ops.input[0], ops.input[1], 				ops.output[0]);
        }
        if (strcmp(ops.gate_type, "OR") == 0) 
	{
         OR(vals, ops.input[0], ops.input[1], 				ops.output[0]);
        }
	if (strcmp(ops.gate_type, "NOT") ==0)
	{
         NOT(vals, ops.input[0], ops.output[0]);
        }
        if (strcmp(ops.gate_type, "NAND") == 0) 
	{
         NAND(vals, ops.input[0], ops.input[1], 			ops.output[0]);
        }
        if (strcmp(ops.gate_type, "NOR") == 0) 
	{
        NOR(vals, ops.input[0], ops.input[1], 				ops.output[0]);
        }
        if (strcmp(ops.gate_type, "XOR") == 0) 
	{
        XOR(vals, ops.input[0], ops.input[1], 				ops.output[0]);
        }
	if (strcmp(ops.gate_type, "DECODER") == 0) 
	{
         DECODER(vals, ops.num, ops.input, ops.output);
        }
	if (strcmp(ops.gate_type, "MULTIPLEXER") == 0) 
	{
        MULTIX(vals, ops.s, ops.input, ops.sel, 			ops.output[0]);
        }
}

for (i = 0; i < output_count; i++) 
{
      printf(" %d", vals[input_count + i + 2]);
}
        if (!increment_input(vals, input_count)) 
	{
            break;
        }
 	printf("\n");
}
	for (i = 0; i < input_count; i++) 
	{
        free(names[i+2]);	
	}

	for (i = 0; i < output_count; i++) 
	{
        free(names[i + input_count+ 2]);
	}
	//free(names[size-1]);
	free(names);
	free(vals);
exit(EXIT_SUCCESS);
}
