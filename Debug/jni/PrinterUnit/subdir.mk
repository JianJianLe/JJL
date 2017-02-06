################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../jni/PrinterUnit/com_djman_admachine_PrinterUnit.c 

OBJS += \
./jni/PrinterUnit/com_djman_admachine_PrinterUnit.o 

C_DEPS += \
./jni/PrinterUnit/com_djman_admachine_PrinterUnit.d 


# Each subdirectory must supply rules for building sources it contributes
jni/PrinterUnit/%.o: ../jni/PrinterUnit/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cygwin C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


