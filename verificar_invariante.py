import re

def verificar_invariante_plaza(log_path):
    invariante_ok = True
    with open(log_path) as f:
        for line in f:
            match = re.search(r"P0:(\d+).*P1:(\d+).*P2:(\d+)", line)
            if match:
                p0, p1, p2 = map(int, match.groups())
                if p0 + p1 + p2 != 3:
                    print(f"Invariante violado en línea: {line.strip()}")
                    invariante_ok = False
    if invariante_ok:
        print("¡El invariante de plaza se cumple en todo el log!")
    else:
        print("Hubo violaciones del invariante.")

if __name__ == "__main__":
    verificar_invariante_plaza("petri_log.txt")


#python3 verificar_invariante.py