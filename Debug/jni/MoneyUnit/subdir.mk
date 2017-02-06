################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../jni/MoneyUnit/com_djman_admachine_MoneyUnit.c 

OBJS += \
./jni/MoneyUnit/com_djman_admachine_MoneyUnit.o 

C_DEPS += \
./jni/MoneyUnit/com_djman_admachine_MoneyUnit.d 


# Each subdirectory must supply rules for building sources it contributes
jni/MoneyUnit/%.o: ../jni/MoneyUnit/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cygwin C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


