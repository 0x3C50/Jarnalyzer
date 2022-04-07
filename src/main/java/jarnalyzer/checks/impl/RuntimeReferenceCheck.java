package jarnalyzer.checks.impl;

import jarnalyzer.checks.Check;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Mnemonic;
import javassist.expr.MethodCall;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RuntimeReferenceCheck implements Check {

    @Override
    public void checkJarEntry(JarFile src, JarEntry je) {

    }

    @Override
    public void checkClassFile(CtClass classFile) throws Exception {
        for (CtMethod declaredMethod : classFile.getDeclaredMethods()) {

            CodeAttribute ca = declaredMethod.getMethodInfo().getCodeAttribute();
            if (ca == null) return;
            CodeIterator ci = ca.iterator();
            while(ci.hasNext()) {
                int i = ci.next();
                int op = ci.byteAt(i);
                if (Mnemonic.OPCODE[op].startsWith("invoke")) {

                }
            }
        }
    }
}
