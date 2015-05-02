#include <dlfcn.h>
#include <jni.h>
#include <string>
#include <stdlib.h>
#include <vector>
#include <android/log.h>
#include "mcpelauncher.h"

bool isInServer = false;

class Player;
class Gui {
	public:
	void displayClientMessage(std::string const&);
};
class MinecraftClient {
	public:
	Gui * getGui();
};
class MobEffectInstance {
	public:
	int effect;
	int time;
	int level;
};
typedef struct {
	char filler[200];
	std::string message;
} ChatScreen;

Player * player = NULL;
MinecraftClient * mc = NULL;

void clientMessage(std::string const& msg) {
	mc->getGui()->displayClientMessage(msg);
}

#include <sstream>
//from http://stackoverflow.com/a/236803
std::vector<std::string> &split(const std::string &s, char delim, std::vector<std::string> &elems) {
    std::stringstream ss(s);
    std::string item;
    while (std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}
std::vector<std::string> split(const std::string &s, char delim) {
    std::vector<std::string> elems;
    split(s, delim, elems);
    return elems;
}

void (*effect_real)(Player*, MobEffectInstance*);
void applyEffect(int _effect, int _time, int _level) {
	MobEffectInstance * efct = new MobEffectInstance();
	efct->effect = _effect;
	efct->time = _time*20;
	efct->level = _level-1;
	effect_real(player, efct);
	delete efct;
}

void (*remove_real)(Player*, int);
void (*remove_particle)(Player*);
void removeEffect() {
	for(int i = 1; i<=23; i++)
		remove_real(player, i);
	remove_particle(player);
}

void (*load_real)(MinecraftClient*, Player*);
void loadHook(MinecraftClient * clazz, Player * p) {
	player = p;
	mc = clazz;
	load_real(clazz, p);
	clientMessage("Single Potion addon by Affogatoman (Will be disabled in server)");
}

void (*send_real)(ChatScreen*);
void sendHook(ChatScreen * screen) {
	std::string message = screen->message;
	if(message.substr(0, 7) == ".effect" && message.size() != 7) {
		if(!isInServer) {
			if(message.substr(7, 13) == " clear") {
				removeEffect();
				clientMessage("§6All effects were cleared");
				return;
			}
			//.effect effect time level
			std::vector<std::string> vect = split(message.substr(8, message.size()), ' ');
			if(vect.size() == 3) {
				std::string effect = vect[0];
				if(effect == "speed" || effect == "1")
					effect = "1";
				else if(effect == "slowness" || effect == "2")
					effect = "2";
				else if(effect == "haste" || effect == "3")
					effect = "3";
				else if(effect == "mining_fatigue" || effect == "4")
					effect = "4";
				else if(effect == "strength" || effect == "5")
					effect = "5";
				else if(effect == "instant_health" || effect == "6")
					effect = "6";
				else if(effect == "instant_damage" || effect == "7")
					effect = "7";
				else if(effect == "jump" || effect == "8")
					effect = "8";
				else if(effect == "nausea" || effect == "9")
					effect = "9";
				else if(effect == "regeneration" || effect == "10")
					effect = "10";
				else if(effect == "damage_resistance" || effect == "11")
					effect = "11";
				else if(effect == "fire_resistance" || effect == "12")
					effect = "12";
				else if(effect == "water_breathing" || effect == "13")
					effect = "13";
				else if(effect == "invisibility" || effect == "14")
					effect = "14";
				else if(effect == "blindness" || effect == "15")
					effect = "15";
				else if(effect == "night_vision" || effect == "16")
					effect = "16";
				else if(effect == "hunger" || effect == "17")
					effect = "17";
				else if(effect == "weakness" || effect == "18")
					effect = "18";
				else if(effect == "poison" || effect == "19")
					effect = "19";
				else if(effect == "wither" || effect == "20")
					effect = "20";
				else if(effect == "health_boost" || effect == "21")
					effect = "21";
				else if(effect == "absorption" || effect == "22")
					effect = "22";
				else if(effect == "saturation" || effect == "23")
					effect = "23";
				else {
					clientMessage("§4There is no effect : "+effect);
					return;
				}
				applyEffect(atoi(effect.c_str()), atoi(vect[1].c_str()), atoi(vect[2].c_str()));
			} else {
				clientMessage("§6.effect [Effect] [Time(s)] [Level]");
				return;
			}
		}
	} else
		send_real(screen);
}

void (*leave_real)(void*, bool);
void leaveHook(void* clazz, bool idk) {
	isInServer = false;
	leave_real(clazz, idk);
}

void (*multi_real)(void*, const char *, int);
void multiHook(void* clazz, const char * ip, int a) {
	isInServer = true;
	multi_real(clazz, ip, a);
}

JNIEXPORT jint JNI_OnLoad(JavaVM * vm, void * reserved) {
	effect_real = (void (*)(Player*,MobEffectInstance*)) dlsym(RTLD_DEFAULT, "_ZN3Mob9addEffectERK17MobEffectInstance");
	remove_real = (void (*)(Player*,int)) dlsym(RTLD_DEFAULT, "_ZN3Mob12removeEffectEi");
	remove_particle = (void (*)(Player*)) dlsym(RTLD_DEFAULT, "_ZN3Mob21removeEffectParticlesEv");
	void * load = dlsym(RTLD_DEFAULT, "_ZN15MinecraftClient14onPlayerLoadedER6Player");
	mcpelauncher_hook(load, (void*) &loadHook, (void**) &load_real);
	void * send = dlsym(RTLD_DEFAULT, "_ZN10ChatScreen15sendChatMessageEv");
	mcpelauncher_hook(send, (void*) &sendHook, (void**) &send_real);
	void * leave = dlsym(RTLD_DEFAULT, "_ZN15MinecraftClient9leaveGameEb");
	mcpelauncher_hook(leave, (void*) &leaveHook, (void**) &leave_real);
	void * multi = dlsym(RTLD_DEFAULT, "_ZN14RakNetInstance7connectEPKci");
	mcpelauncher_hook(multi, (void*) &multiHook, (void**) &multi_real);
	return JNI_VERSION_1_2;
}